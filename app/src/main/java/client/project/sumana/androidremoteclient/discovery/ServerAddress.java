package client.project.sumana.androidremoteclient.discovery;

/**
 * Created by Karthik A on 03-05-16.
 */

import java.net.InetAddress;

/**
 * Holds information about a media server which announced itself in response to
 * a discovery request.
 */
public final class ServerAddress {
    private final String mName;
    private final String mAddr;

    public ServerAddress(String name, String address) {
        mAddr = address;
        mName = name;
    }

    public String getmAddr() {
        return mAddr;
    }

    public String getmName() {
        return mName;
    }

    public String toString() {
        return String.format("%s at %s", mName, mAddr);
    }
}
