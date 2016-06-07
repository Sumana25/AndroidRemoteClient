package client.project.sumana.androidremoteclient.buttonfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import client.project.sumana.androidremoteclient.R;
import client.project.sumana.androidremoteclient.constants.Constants;
import client.project.sumana.androidremoteclient.http.HTTPRequestClient;
import client.project.sumana.androidremoteclient.videoplayer.MP3PlayerActivity;
import client.project.sumana.androidremoteclient.videoplayer.VideoPlayerActivity;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ButtonFragment#newInstance} factory method to
 * create an instance of this fragmen   t.
 */
public class ButtonFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button notepadButton, videoPlayerButton, mp3Button, switchWindowButton, calculatorButton, fileExplorerButton, photosButton, googleButton, pageDownButton, pageUpButton, closeWindowButton, homeButton, endButton, enterButton;
    Button nextTab, prevTab, nextProg, prevProg, closeTab;

    public ButtonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ButtonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ButtonFragment newInstance(String param1, String param2) {
        ButtonFragment fragment = new ButtonFragment();
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
        View v = inflater.inflate(R.layout.fragment_button, container, false);
        notepadButton = (Button) v.findViewById(R.id.notepad_button);
        videoPlayerButton = (Button) v.findViewById(R.id.video_player__button);
        mp3Button = (Button) v.findViewById(R.id.mp3_button);
        switchWindowButton = (Button) v.findViewById(R.id.switch_window_button);
        calculatorButton = (Button) v.findViewById(R.id.calculator_button);
        fileExplorerButton = (Button) v.findViewById(R.id.file_explorer_button);
        photosButton = (Button) v.findViewById(R.id.photos_button);
        googleButton = (Button) v.findViewById(R.id.google_button);
        pageDownButton = (Button) v.findViewById(R.id.page_down_button);
        pageUpButton = (Button) v.findViewById(R.id.page_up_button);
        closeWindowButton = (Button) v.findViewById(R.id.close_window_button);
        homeButton = (Button) v.findViewById(R.id.home_button);
        endButton = (Button) v.findViewById(R.id.end_button);
        enterButton = (Button) v.findViewById(R.id.enter_button);
        nextTab = (Button) v.findViewById(R.id.ctrl_tab);
        prevTab = (Button) v.findViewById(R.id.ctrl_shift_tab);
        nextProg = (Button) v.findViewById(R.id.alt_tab);
        prevProg = (Button) v.findViewById(R.id.alt_shift_tab);
        closeTab = (Button) v.findViewById(R.id.close_tab);

        notepadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("open_notepad");
            }
        });

        videoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), VideoPlayerActivity.class);
                startActivity(i);
            }
        });

        mp3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MP3PlayerActivity.class);
                startActivity(i);
            }
        });

        switchWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("switch_window");
            }
        });

        calculatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("calculator");
            }
        });

        fileExplorerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("explorer");
            }
        });

        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("photos");
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("google");
            }
        });

        pageDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("page_down");
            }
        });

        pageUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("page_up");
            }
        });

        closeWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("close_window");
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("home");
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("end");
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("enter");
            }
        });

        nextTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("next_tab");
            }
        });

        prevTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("prev_tab");
            }
        });

        nextProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("next_prog");
            }
        });

        prevProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("prev_prog");
            }
        });

        closeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAction("close_tab");
            }
        });

        return v;
    }


    public void doAction(String action) {
        HTTPRequestClient.get(Constants.getAbsoluteUrl("/do_action?action="+action, getContext()),
                null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //Success code
                        try {
                            Toast.makeText(getActivity(), new String(responseBody, "UTF-8"), Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Failure code
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

