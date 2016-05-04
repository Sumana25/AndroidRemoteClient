package client.project.sumana.androidremoteclient.gesturefragment;


import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import client.project.sumana.androidremoteclient.R;
import client.project.sumana.androidremoteclient.constants.Constants;
import client.project.sumana.androidremoteclient.http.HTTPRequestClient;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GestureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestureFragment extends Fragment implements  OnGestureDetected{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final int MOTION_UP = 0x1;
    private final int MOTION_DOWN = 0x2;
    private final int MOTION_LEFT = 0x3;
    private final int MOTION_RIGHT = 0x4;

    private GestureDetector mGestureDetector;


    public GestureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GestureFragment newInstance(String param1, String param2) {
        GestureFragment fragment = new GestureFragment();
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
        View v = inflater.inflate(R.layout.fragment_gesture, container, false);
        GestureOverlayView gestureOverlayView = (GestureOverlayView) v.findViewById(R.id.gesturepanel);
        gestureOverlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        Android_Gesture_Detector  android_gesture_detector  =  new Android_Gesture_Detector();
        android_gesture_detector.setOnGestureDetected(this);
        mGestureDetector = new GestureDetector(getContext(), android_gesture_detector);
        return v;
    }

    class Android_Gesture_Detector implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        private float SWIPE_THRESHOLD = 100f;
        private OnGestureDetected onGestureDetected = null;

        public void setOnGestureDetected(OnGestureDetected onGestureDetected) {
            this.onGestureDetected = onGestureDetected;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("Gesture ", " onDown");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            Log.d("Gesture", "Y: "+diffY+" / "+ "X: "+diffX);

            if(Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) >= SWIPE_THRESHOLD) {
                int motion = 0;
                if (e1.getX() < e2.getX()) {
                    Log.d("Gesture ", "Left to Right swipe: "+ e1.getX() + " - " + e2.getX());
                    Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
                    motion = MOTION_RIGHT;
                }
                if (e1.getX() >= e2.getX()) {
                    Log.d("Gesture ", "Right to Left swipe: "+ e1.getX() + " - " + e2.getX());
                    Log.d("Speed ", String.valueOf(velocityX) + " pixels/second");
                    motion = MOTION_LEFT;
                }

                if(onGestureDetected != null) {
                    onGestureDetected.handleGesture(motion);
                }

                return true;
            } else if(Math.abs(diffY) > Math.abs(diffX) && Math.abs(diffY) >= SWIPE_THRESHOLD) {
                int motion = 0;
                if (e1.getY() < e2.getY()) {
                    Log.d("Gesture ", "Up to Down swipe: " + e1.getX() + " - " + e2.getX());
                    Log.d("Speed ", String.valueOf(velocityY) + " pixels/second");
                    motion = MOTION_DOWN;
                }
                if (e1.getY() >= e2.getY()) {
                    Log.d("Gesture ", "Down to Up swipe: " + e1.getX() + " - " + e2.getX());
                    Log.d("Speed ", String.valueOf(velocityY) + " pixels/second");
                    motion = MOTION_UP;
                }
                if(onGestureDetected != null) {
                    onGestureDetected.handleGesture(motion);
                }

                return true;
            }





            return false;
        }
    }


    @Override
    public void handleGesture(int gesture) {
        switch(gesture) {
            case MOTION_LEFT: runGestureCommand("explorer",""); break;
            case MOTION_RIGHT: runGestureCommand("calculator",""); break;
            case MOTION_UP: runGestureCommand("photos",""); break;
            case MOTION_DOWN: runGestureCommand("close_window",""); break;
        }
    }

    public void runGestureCommand(String command, String arg) {
        HTTPRequestClient.get(Constants.getAbsoluteUrl("/do_action?action="+command+"&arg="+arg, getContext()),
                null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
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
