package client.project.sumana.androidremoteclient.discovery;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.provider.SyncStateContract;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import client.project.sumana.androidremoteclient.constants.Constants;

public class DiscoveryThread extends Thread {
    public static final String TAG = "Discoverer";
    private static final int TIMEOUT_MS = 750;
    private Receiver mReceiver;
    private WifiManager mWifi;
    private boolean mListening = true;
    private DatagramSocket mSocket;
    private Context mContext;

    public interface Receiver {
        /**
         * Process the list of discovered servers. This is always called once
         * after a short timeout.
         *
         * @param servers
         *            list of discovered servers, null on error
         */
        void addAnnouncedServers(ArrayList<ServerAddress> servers);
    }

    public DiscoveryThread(Context context, Receiver receiver) {
        mContext = context;
        mWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mReceiver = receiver;
    }

    public void setReceiver(Receiver r)
    {
        mReceiver = r;
        if (mReceiver == null && mSocket != null) mSocket.close();
    }

    public void run() {
        ArrayList<ServerAddress> servers = null;
        mSocket = null;
        try {
            mSocket = new DatagramSocket(Constants.DISCOVERY_PORT);
            //we could have two or more discoverers at the same time.
            mSocket.setReuseAddress(true);
            mSocket.setBroadcast(true);
            mSocket.setSoTimeout(TIMEOUT_MS);

            sendDiscoveryRequest(mSocket);
            servers = listenForResponses(mSocket);
        } catch (IOException e) {
            servers = new ArrayList<ServerAddress>(); // use an empty one
            Log.e(TAG, "Could not send discovery request", e);
        }
        finally
        {
            Receiver r = mReceiver;
            if (r != null)
                r.addAnnouncedServers(servers);
            Log.d(TAG, servers.toString());
            mListening = false;
            if (mSocket != null) mSocket.close();
        }
    }

    /**
     * Send a broadcast UDP packet containing a request for boxee services to
     * announce themselves.
     *
     * @throws IOException
     */
    private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {
        String data = Constants.DISCOVERY_MESSAGE;
        Log.d(TAG, "Sending data " + data);
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data
                .length(), getBroadcastAddress(), Constants.DISCOVERY_PORT);
        socket.send(packet);
    }

    /**
     * Calculate the broadcast IP we need to send the packet along. If we send
     * it to 255.255.255.255, it never gets sent. I guess this has something to
     * do with the mobile network not wanting to do broadcast.
     */
    public InetAddress getBroadcastAddress() throws IOException {
        DhcpInfo dhcp = mWifi.getDhcpInfo();
        if (dhcp == null) {
            Log.d(TAG, "Could not get dhcp info");
            return null;
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);

        return InetAddress.getByAddress(quads);
    }

    /**
     * Listen on socket for responses, timing out after TIMEOUT_MS
     *
     * @param socket
     *            socket on which the announcement request was sent
     * @return list of discovered servers, never null
     * @throws IOException
     */
    private ArrayList<ServerAddress> listenForResponses(DatagramSocket socket)
            throws IOException {
        long start = System.currentTimeMillis();
        byte[] buf = new byte[10240];
        ArrayList<ServerAddress> servers = new ArrayList<ServerAddress>();

        try {
            while (mReceiver != null) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                Log.d(TAG, "Waiting for discovery response...");
                socket.receive(packet);
                String s = new String(packet.getData(), packet.getOffset(), packet.getLength());
                if(s.equals(Constants.DISCOVERY_MESSAGE)) {
                    continue;
                }
                Log.d(TAG, "Packet received after "+ (System.currentTimeMillis() - start) + " " + s);
                InetAddress sourceAddress = packet.getAddress();
                Log.d(TAG, "Parsing response from "+packet.getAddress().getHostAddress()+"...:" + s);
                Constants.putServerAddress(mContext, packet.getAddress().getHostAddress());

            }
        } catch (SocketTimeoutException e) {
            Log.w(TAG, "Receive timed out");
        }
        catch(Exception e)
        {
            Log.w(TAG, "Failed to create a Server object from discovery response! Error: "+e.getMessage());
            e.printStackTrace();
        }
        return servers;
    }



    public boolean isDiscoverying() {
        return mListening && mReceiver != null;
    }


}