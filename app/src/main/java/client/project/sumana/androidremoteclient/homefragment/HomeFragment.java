package client.project.sumana.androidremoteclient.homefragment;


import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;
import java.util.concurrent.RunnableFuture;

import client.project.sumana.androidremoteclient.R;
import client.project.sumana.androidremoteclient.constants.Constants;
import client.project.sumana.androidremoteclient.discovery.DiscoveryThread;
import client.project.sumana.androidremoteclient.discovery.ServerAddress;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ShakeDetector.Listener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView connectionInfo;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Button discoverButton = (Button) v.findViewById(R.id.discover_button);
        Button disconnectButton = (Button) v.findViewById(R.id.disconnect_button);
        connectionInfo = (TextView) v.findViewById(R.id.connection_info);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateConnection();
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectConnection();
            }
        });
        setConnectionInfo();

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        return v;
    }

    public void initiateConnection() {
        new DiscoveryThread(getContext(), new DiscoveryThread.Receiver() {
            @Override
            public void addAnnouncedServers(final ArrayList<ServerAddress> servers) {
                Log.d(DiscoveryThread.TAG, servers.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Found "+servers.size()+ " server(s)", Toast.LENGTH_SHORT).show();
                        if(servers.size() > 0) {
                            ServerAddress s = servers.get(0);
                            Toast.makeText(getContext(), s.getmAddr()+"/"+s.getmName(), Toast.LENGTH_SHORT).show();
                            Constants.putServerName(getContext(), s.getmName());
                            Constants.putServerAddress(getContext(), s.getmAddr());
                        }
                        setConnectionInfo();
                    }
                });

            }
        }).start();
    }

    public void disconnectConnection() {
        Toast.makeText(getContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        Constants.putServerAddress(getContext(), null);
        Constants.putServerName(getContext(), null);
        setConnectionInfo();
    }

    public void setConnectionInfo() {
        String serverName = Constants.getServerName(getContext());
        String serverAddress = Constants.getServerAddress(getContext());
        if(serverAddress == null || serverName == null || serverAddress.equals("") || serverName.equals("")) {
            connectionInfo.setText("Not connected to server");
        } else {
            connectionInfo.setText("Connected to "+ serverName + " ("+serverAddress+")");
        }
    }

    @Override
    public void hearShake() {

        if(getContext() == null) {
            return;
        }

        if(Constants.isServerConnected(getContext())) {
            disconnectConnection();
        } else {
            initiateConnection();
        }
    }
}
