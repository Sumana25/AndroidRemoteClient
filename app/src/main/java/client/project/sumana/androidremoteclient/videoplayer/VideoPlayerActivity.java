package client.project.sumana.androidremoteclient.videoplayer;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import client.project.sumana.androidremoteclient.R;
import client.project.sumana.androidremoteclient.constants.Constants;
import client.project.sumana.androidremoteclient.http.HTTPRequestClient;
import cz.msebera.android.httpclient.Header;

public class VideoPlayerActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> videoList;
    ImageView play, pause, volup, voldown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadVideos();

        play = (ImageView) findViewById(R.id.video_play);
        pause = (ImageView) findViewById(R.id.video_pause);
        volup = (ImageView) findViewById(R.id.video_volup);
        voldown = (ImageView) findViewById(R.id.video_voldown);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCommand("play", "");
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCommand("pause", "");
            }
        });

        voldown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCommand("voldown", "");
            }
        });

        volup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCommand("volup", "");
            }
        });



        listView = (ListView) findViewById(R.id.video_listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String video = videoList.get(position);
                runCommand("play_video", video);
            }
        });

    }

    public void loadVideos() {
        HTTPRequestClient.get(Constants.getAbsoluteUrl("/do_action?action="+"videos", this),
                null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String s = new String(responseBody, "UTF-8");
                            JSONArray videos = new JSONArray(s);
                            videoList = new ArrayList<String>();
                            for(int i=0; i<videos.length(); i++) {
//                                Toast.makeText(VideoPlayerActivity.this, videos.get(i).toString(), Toast.LENGTH_SHORT).show();
                                videoList.add(videos.get(i).toString());
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(VideoPlayerActivity.this, R.layout.list_item, videoList.toArray(new String[videoList.size()]));
                            listView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Failure code
                        Toast.makeText(VideoPlayerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void runCommand(String command, String arg) {
        HTTPRequestClient.get(Constants.getAbsoluteUrl("/do_action?action="+command+"&arg="+arg, this),
                null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            Toast.makeText(VideoPlayerActivity.this, new String(responseBody, "UTF-8"), Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        //Failure code
                        Toast.makeText(VideoPlayerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
