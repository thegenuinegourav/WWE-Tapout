package thegenuinegourav.wwetapout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class EntranceActivity extends Activity {
    private VideoView p1,p2;
    private String one_p_name, two_p_name;
    private int value1, value2,stopPosition;
    private boolean whichVideo;
    private TextView skip_1p_video, skip_2p_video,vs_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        whichVideo=false; //Indicating 1st video is playing
        setContentView(R.layout.activity_entrance);
        p1 = (VideoView) findViewById(R.id.player_one_video);
        p2 = (VideoView) findViewById(R.id.player_two_video);
        skip_1p_video = (TextView) findViewById(R.id.skip_1p_video);
        skip_2p_video = (TextView) findViewById(R.id.skip_2p_video);
        vs_text = (TextView) findViewById(R.id.vs_text);

        one_p_name = getIntent().getExtras().getString("one_p_name");
        two_p_name = getIntent().getExtras().getString("two_p_name");
        value1 = getIntent().getExtras().getInt("one_p_power");
        value2 = getIntent().getExtras().getInt("two_p_power");

        skip_1p_video.setVisibility(View.GONE);
        skip_1p_video.setClickable(false);
        skip_2p_video.setVisibility(View.GONE);
        skip_2p_video.setClickable(false);
        vs_text.setVisibility(View.GONE);

        Uri uri_p1=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/" + one_p_name + ".wmv");
        final Uri uri_p2=Uri.parse(Environment.getExternalStorageDirectory().getPath() +"/" + two_p_name + ".wmv");

        p1.setVideoURI(uri_p1);
        p1.requestFocus();
        p2.setVideoURI(uri_p2);
        p2.requestFocus();

        p1.start();
        p1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                whichVideo=true; // Indicating second video
                skip_1p_video.setVisibility(View.GONE);
                skip_1p_video.setClickable(false);
                p2.start();
                addSkipFor2pVideo();
            }
        });
        p2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                startGame();
            }
        });

        skip_1p_video.postDelayed(new Runnable() {
            @Override
            public void run() {
                skip_1p_video.setVisibility(View.VISIBLE);
                skip_1p_video.setClickable(true);
                skip_1p_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        skip_1p_video.setVisibility(View.GONE);
                        skip_1p_video.setClickable(false);
                        p1.pause();
                        whichVideo=true;
                        p2.start();
                        addSkipFor2pVideo();
                    }
                });
            }
        },4000);


    }

    public void addSkipFor2pVideo() {
        vs_text.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceIn)
                .duration(1500)
                .playOn(vs_text);
        skip_2p_video.postDelayed(new Runnable() {
            @Override
            public void run() {
                skip_2p_video.setVisibility(View.VISIBLE);
                skip_2p_video.setClickable(true);
                skip_2p_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startGame();
                    }
                });
            }
        },4000);
    }

    public void startGame(){
        Intent i = new Intent(EntranceActivity.this,GamePlayActivity.class);
        i.putExtra("one_p_name",one_p_name);
        i.putExtra("one_p_power",value1);
        i.putExtra("two_p_name",two_p_name);
        i.putExtra("two_p_power",value2);
        startActivity(i);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!whichVideo) {
            stopPosition = p1.getCurrentPosition(); //stopPosition is an int
            p1.pause();
        }else{
            stopPosition = p2.getCurrentPosition(); //stopPosition is an int
            p2.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!whichVideo) {
            p1.seekTo(stopPosition);
            p1.start(); //Or use resume() if it doesn't work. I'm not sure
        }else {
            p2.seekTo(stopPosition);
            p2.start(); //Or use resume() if it doesn't work. I'm not sure
        }
    }
}
