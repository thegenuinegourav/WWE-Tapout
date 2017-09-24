package thegenuinegourav.wwetapout;

import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.yalantis.starwars.TilesFrameLayout;
import com.zys.brokenview.BrokenCallback;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;
import java.util.Random;

public class GamePlayActivity extends Activity {
    private int value1,value2,score1,score2;
    int[][] gameMatrix;
    boolean[][] repMatrix;
    private Button button00,button01,button02,button03;
    private Button button10,button11,button12,button13;
    private Button button20,button21,button22,button23;
    private Button button30,button31,button32,button33;
    private Button button40,button41,button42,button43;
    private Button button50,button51,button52,button53;
    private Button status,tap_to_continue;
    private String one_p_name, two_p_name;
    private int one_p_res, two_p_res,stopPosition;
    private BrokenTouchListener listener;
    private BrokenView brokenView;
    private SoundPool glass_break,bell,slap,punch;
    private int punchID,glassID,bellID,slapID;
    boolean loaded_glass,loaded_bell,loaded_slap,loaded_punch;
    private AudioManager audioManager;
    private float actualVolume, maxVolume, volume;
    private VideoView videoView;
    private String winner_name;
    private TilesFrameLayout mTilesFrameLayout;
    private ImageView winner_image,loser_image,tlstar,trstar,brstar,blstar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        brokenView = BrokenView.add2Window(this);
        listener = new BrokenTouchListener.Builder(brokenView).build();

        one_p_name = getIntent().getExtras().getString("one_p_name");
        two_p_name = getIntent().getExtras().getString("two_p_name");
        value1 = getIntent().getExtras().getInt("one_p_power");
        value2 = getIntent().getExtras().getInt("two_p_power");

        loaded_glass=loaded_bell=loaded_slap=loaded_punch=false;

        loadAllSoundPool();

        init();

        one_p_res = findDrawableResourceFromName(one_p_name);
        two_p_res = findDrawableResourceFromName(two_p_name);

        repMatrix = new boolean[6][4];
        gameMatrix = new int[6][4];
        score1=score2=12;
        for(int i=0;i<6;i++) {
            for(int j=0;j<4;j++) {
                gameMatrix[i][j]=value1;
                repMatrix[i][j]=false;
            }
        }
        for(int i=0;i<12;i++) {
            Random r = new Random();
            int row = r.nextInt(6);
            int col = r.nextInt(4);
            while(repMatrix[row][col]) {
                row = r.nextInt(6);
                col = r.nextInt(4);
            }
            repMatrix[row][col]=true;
            gameMatrix[row][col]=value2;
        }
        updateButtonsText();
        bell.play(bellID,volume,volume,1,0,1f);
        brokenView.setCallback(new BrokenCallback() {

            @Override
            public void onFalling(View v) {
                if(loaded_glass) glass_break.play(glassID,volume,volume,1,0,1f);
                int r=-1,c=-1;
                switch (v.getId()) {
                    case R.id.button00: r=0; c=0; break;
                    case R.id.button01: r=0; c=1; break;
                    case R.id.button02: r=0; c=2; break;
                    case R.id.button03: r=0; c=3; break;
                    case R.id.button10: r=1; c=0; break;
                    case R.id.button11: r=1; c=1; break;
                    case R.id.button12: r=1; c=2; break;
                    case R.id.button13: r=1; c=3; break;
                    case R.id.button20: r=2; c=0; break;
                    case R.id.button21: r=2; c=1; break;
                    case R.id.button22: r=2; c=2; break;
                    case R.id.button23: r=2; c=3; break;
                    case R.id.button30: r=3; c=0; break;
                    case R.id.button31: r=3; c=1; break;
                    case R.id.button32: r=3; c=2; break;
                    case R.id.button33: r=3; c=3; break;
                    case R.id.button40: r=4; c=0; break;
                    case R.id.button41: r=4; c=1; break;
                    case R.id.button42: r=4; c=2; break;
                    case R.id.button43: r=4; c=3; break;
                    case R.id.button50: r=5; c=0; break;
                    case R.id.button51: r=5; c=1; break;
                    case R.id.button52: r=5; c=2; break;
                    case R.id.button53: r=5; c=3; break;
                }
                if(r!=-1 && c!=-1) {
                    if(!repMatrix[r][c]) score1--;
                    else score2--;
                }
                check();
            }

            @Override
            public void onFallingEnd(View v) {}
        });

        videoView =(VideoView)findViewById(R.id.winnerVideo);

        mTilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tiles_frame_layout);
    }

    public void init() {
        button00 = (Button) findViewById(R.id.button00);
        button01 = (Button) findViewById(R.id.button01);
        button02 = (Button) findViewById(R.id.button02);
        button03 = (Button) findViewById(R.id.button03);

        button10 = (Button) findViewById(R.id.button10);
        button11 = (Button) findViewById(R.id.button11);
        button12 = (Button) findViewById(R.id.button12);
        button13 = (Button) findViewById(R.id.button13);

        button20 = (Button) findViewById(R.id.button20);
        button21 = (Button) findViewById(R.id.button21);
        button22 = (Button) findViewById(R.id.button22);
        button23 = (Button) findViewById(R.id.button23);

        button30 = (Button) findViewById(R.id.button30);
        button31 = (Button) findViewById(R.id.button31);
        button32 = (Button) findViewById(R.id.button32);
        button33 = (Button) findViewById(R.id.button33);

        button40 = (Button) findViewById(R.id.button40);
        button41 = (Button) findViewById(R.id.button41);
        button42 = (Button) findViewById(R.id.button42);
        button43 = (Button) findViewById(R.id.button43);

        button50 = (Button) findViewById(R.id.button50);
        button51 = (Button) findViewById(R.id.button51);
        button52 = (Button) findViewById(R.id.button52);
        button53 = (Button) findViewById(R.id.button53);

        status = (Button) findViewById(R.id.who_is_wining_status);
        tap_to_continue = (Button) findViewById(R.id.tap_to_continue);
        winner_image = (ImageView) findViewById(R.id.winner_image);
        loser_image = (ImageView) findViewById(R.id.loser_image);
        //winner_belt_image = (ImageView) findViewById(R.id.winner_belt_image);
        tlstar = (ImageView) findViewById(R.id.top_left_star);
        trstar = (ImageView) findViewById(R.id.top_right_star);
        blstar = (ImageView) findViewById(R.id.bottom_left_star);
        brstar = (ImageView) findViewById(R.id.bottom_right_star);
    }

    @Override
    public void onResume() {
        super.onResume();
        mTilesFrameLayout.onResume();
        videoView.seekTo(stopPosition);
        videoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTilesFrameLayout.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }

    public int findDrawableResourceFromName(String name) {
        switch (name) {
            case "bray" : return R.drawable.bray;
            case "becky" : return R.drawable.becky;
            case "dean" : return R.drawable.dean;
            case "big_show" : return R.drawable.big_show;
            case "bayley" : return R.drawable.bayley;
            case "john" : return R.drawable.john;
            case "samoa" : return R.drawable.samoa;
            case "alexa" : return R.drawable.alexa;
            case "shinsuke" : return R.drawable.shinsuke;
            case "finn" : return R.drawable.finn;
            case "sasha" : return R.drawable.sasha;
            case "kevin" : return R.drawable.kevin;
            case "aj" : return R.drawable.aj;
            case "roman" : return R.drawable.roman;
            case "undertaker" : return R.drawable.undertaker;
            case "seth" : return R.drawable.seth;
            case "brock" : return R.drawable.brock;
            case "charlotte" : return R.drawable.charlotte;
            case "asuka" : return R.drawable.asuka;
            case "braun" : return R.drawable.braun;
            default: return -1;
        }
    }

    public void check() {
        if(score1==0 || score2==0) {
            if(loaded_bell) bell.play(bellID,volume,volume,1,0,1f);
            if(score1==0) winner_name=two_p_name;
            if(score2==0) winner_name=one_p_name;
            mTilesFrameLayout.startAnimation();
            startWinnerLayout();
        }
    }

    public void startWinnerLayout(){

        winner_image.postDelayed(new Runnable() {
            @Override
            public void run() {
                winner_image.setVisibility(View.VISIBLE);
                winner_image.setImageResource(one_p_res);
                YoYo.with(Techniques.ZoomInLeft)
                        .duration(1400)
                        .playOn(winner_image);
            }
        },1000);
        loser_image.postDelayed(new Runnable() {
            @Override
            public void run() {
                loser_image.setVisibility(View.VISIBLE);
                loser_image.setImageResource(two_p_res);
                YoYo.with(Techniques.ZoomInRight)
                        .duration(1400)
                        .playOn(loser_image);
            }
        },1000);

        status.postDelayed(new Runnable() {
            @Override
            public void run() {
                status.setVisibility(View.VISIBLE);
            }
        },500);

        status.postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomOutUp)
                        .duration(1400)
                        .playOn(status);
            }
        },4000);

        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.setVisibility(View.VISIBLE);
                Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/" + winner_name + ".wmv");
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();
                YoYo.with(Techniques.ZoomIn)
                        .duration(1400)
                        .playOn(videoView);
                if(winner_name.equals(one_p_name)) loser_image.setAlpha(0.4f);
                else winner_image.setAlpha(0.4f);
            }
        },4500);

        status.postDelayed(new Runnable() {
            @Override
            public void run() {
                status.setText(winner_name);
                YoYo.with(Techniques.StandUp)
                        .duration(1400)
                        .playOn(status);
            }
        },5500);

        trstar.postDelayed(new Runnable() {
            @Override
            public void run() {
                trstar.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Flash)
                        .duration(1400)
                        .repeat(Integer.MAX_VALUE)
                        .playOn(trstar);
            }
        },5500);

        blstar.postDelayed(new Runnable() {
            @Override
            public void run() {
                blstar.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Flash)
                        .duration(800)
                        .repeat(Integer.MAX_VALUE)
                        .playOn(blstar);
            }
        },5500);

        tlstar.postDelayed(new Runnable() {
            @Override
            public void run() {
                tlstar.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Flash)
                        .duration(900)
                        .repeat(Integer.MAX_VALUE)
                        .playOn(tlstar);
            }
        },5500);

        brstar.postDelayed(new Runnable() {
            @Override
            public void run() {
                brstar.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Flash)
                        .duration(1000)
                        .repeat(Integer.MAX_VALUE)
                        .playOn(brstar);
            }
        },5500);

        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.Shake)
                        .duration(1400)
                        .repeat(Integer.MAX_VALUE)
                        .playOn(videoView);
            }
        },6500);

        tap_to_continue.postDelayed(new Runnable() {
            @Override
            public void run() {
                tap_to_continue.setVisibility(View.VISIBLE);
                tap_to_continue.setClickable(true);
                YoYo.with(Techniques.BounceInUp)
                        .duration(1400)
                        .playOn(brstar);
            }
        },7500);

        status.postDelayed(new Runnable() {
            @Override
            public void run() {
                status.setText(winner_name);
                YoYo.with(Techniques.Pulse)
                        .duration(3500)
                        .repeat(Integer.MAX_VALUE)
                        .playOn(status);
            }
        },8000);

        tap_to_continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exit_match_dialog);
        Button yes = (Button) dialog.findViewById(R.id.yes_quit_match);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button no = (Button) dialog.findViewById(R.id.no_quit_match);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void updateButtonsText() {
        if(repMatrix[0][0]) button00.setBackgroundResource(two_p_res);
        else button00.setBackgroundResource(one_p_res);
        if(repMatrix[0][1]) button01.setBackgroundResource(two_p_res);
        else button01.setBackgroundResource(one_p_res);
        if(repMatrix[0][2]) button02.setBackgroundResource(two_p_res);
        else button02.setBackgroundResource(one_p_res);
        if(repMatrix[0][3]) button03.setBackgroundResource(two_p_res);
        else button03.setBackgroundResource(one_p_res);

        if(repMatrix[1][0]) button10.setBackgroundResource(two_p_res);
        else button10.setBackgroundResource(one_p_res);
        if(repMatrix[1][1]) button11.setBackgroundResource(two_p_res);
        else button11.setBackgroundResource(one_p_res);
        if(repMatrix[1][2]) button12.setBackgroundResource(two_p_res);
        else button12.setBackgroundResource(one_p_res);
        if(repMatrix[1][3]) button13.setBackgroundResource(two_p_res);
        else button13.setBackgroundResource(one_p_res);

        if(repMatrix[2][0]) button20.setBackgroundResource(two_p_res);
        else button20.setBackgroundResource(one_p_res);
        if(repMatrix[2][1]) button21.setBackgroundResource(two_p_res);
        else button21.setBackgroundResource(one_p_res);
        if(repMatrix[2][2]) button22.setBackgroundResource(two_p_res);
        else button22.setBackgroundResource(one_p_res);
        if(repMatrix[2][3]) button23.setBackgroundResource(two_p_res);
        else button23.setBackgroundResource(one_p_res);

        if(repMatrix[3][0]) button30.setBackgroundResource(two_p_res);
        else button30.setBackgroundResource(one_p_res);
        if(repMatrix[3][1]) button31.setBackgroundResource(two_p_res);
        else button31.setBackgroundResource(one_p_res);
        if(repMatrix[3][2]) button32.setBackgroundResource(two_p_res);
        else button32.setBackgroundResource(one_p_res);
        if(repMatrix[3][3]) button33.setBackgroundResource(two_p_res);
        else button33.setBackgroundResource(one_p_res);

        if(repMatrix[4][0]) button40.setBackgroundResource(two_p_res);
        else button40.setBackgroundResource(one_p_res);
        if(repMatrix[4][1]) button41.setBackgroundResource(two_p_res);
        else button41.setBackgroundResource(one_p_res);
        if(repMatrix[4][2]) button42.setBackgroundResource(two_p_res);
        else button42.setBackgroundResource(one_p_res);
        if(repMatrix[4][3]) button43.setBackgroundResource(two_p_res);
        else button43.setBackgroundResource(one_p_res);

        if(repMatrix[5][0]) button50.setBackgroundResource(two_p_res);
        else button50.setBackgroundResource(one_p_res);
        if(repMatrix[5][1]) button51.setBackgroundResource(two_p_res);
        else button51.setBackgroundResource(one_p_res);
        if(repMatrix[5][2]) button52.setBackgroundResource(two_p_res);
        else button52.setBackgroundResource(one_p_res);
        if(repMatrix[5][3]) button53.setBackgroundResource(two_p_res);
        else button53.setBackgroundResource(one_p_res);
    }


    public void loadAllSoundPool() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        punch = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        punch.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded_punch = true;
            }
        });
        punchID = punch.load(this, R.raw.punch, 1);

        glass_break = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        glass_break.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded_glass = true;
            }
        });
        glassID = glass_break.load(this, R.raw.glass_break, 1);

        bell = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        bell.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded_bell = true;
            }
        });
        bellID = bell.load(this, R.raw.bell, 1);

        slap = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        slap.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded_slap = true;
            }
        });
        slapID = slap.load(this, R.raw.slap, 1);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;
    }
    
    
    public void Button00(View view) {
        if (gameMatrix[0][0] == 1) {
            button00.setText("Hold");
            button00.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
        } else {
            button00.setText(String.valueOf(gameMatrix[0][0]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
        }
        view.setAlpha(1f - 0.5f/gameMatrix[0][0]);
        gameMatrix[0][0] -= 1;
        
    }

    public void Button01(View view) {
        if (gameMatrix[0][1] == 1) {
            button01.setText("Hold");
            button01.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
        } else {
            button01.setText(String.valueOf(gameMatrix[0][1]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
        }
        view.setAlpha(1f - 0.5f/gameMatrix[0][1]);
        gameMatrix[0][1] -= 1;
        
    }

    public void Button02(View view) {
        if (gameMatrix[0][2] == 1) {
            button02.setText("Hold");
            button02.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button02.setText(String.valueOf(gameMatrix[0][2]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[0][2]);
        gameMatrix[0][2] -= 1;
        
    }

    public void Button03(View view) {
        if (gameMatrix[0][3] == 1) {
            button03.setText("Hold");
            button03.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button03.setText(String.valueOf(gameMatrix[0][3]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[0][3]);
        gameMatrix[0][3] -= 1;
        
    }

    public void Button10(View view) {
        if (gameMatrix[1][0] == 1) {
            button10.setText("Hold");
            button10.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button10.setText(String.valueOf(gameMatrix[1][0]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[1][0]);
        gameMatrix[1][0] -= 1;
        
    }

    public void Button11(View view) {
        if (gameMatrix[1][1] == 1) {
            button11.setText("Hold");
            button11.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button11.setText(String.valueOf(gameMatrix[1][1]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[1][1]);
        gameMatrix[1][1] -= 1;
        
    }

    public void Button12(View view) {
        if (gameMatrix[1][2] == 1) {
            button12.setText("Hold");
            button12.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button12.setText(String.valueOf(gameMatrix[1][2]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[1][2]);
        gameMatrix[1][2] -= 1;
        
    }

    public void Button13(View view) {
        if (gameMatrix[1][3] == 1) {
            button13.setText("Hold");
            button13.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button13.setText(String.valueOf(gameMatrix[1][3]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[1][3]);
        gameMatrix[1][3] -= 1;
        
    }

    public void Button20(View view) {
        if (gameMatrix[2][0] == 1) {
            button20.setText("Hold");
            button20.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button20.setText(String.valueOf(gameMatrix[2][0]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[2][0]);
        gameMatrix[2][0] -= 1;
        
    }

    public void Button21(View view) {
        if (gameMatrix[2][1] == 1) {
            button21.setText("Hold");
            button21.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button21.setText(String.valueOf(gameMatrix[2][1]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[2][1]);
        gameMatrix[2][1] -= 1;
        
    }

    public void Button22(View view) {
        if (gameMatrix[2][2] == 1) {
            button22.setText("Hold");
            button22.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button22.setText(String.valueOf(gameMatrix[2][2]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[2][2]);
        gameMatrix[2][2] -= 1;
        
    }

    public void Button23(View view) {
        if (gameMatrix[2][3] == 1) {
            button23.setText("Hold");
            button23.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button23.setText(String.valueOf(gameMatrix[2][3]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[2][3]);
        gameMatrix[2][3] -= 1;
        
    }

    public void Button30(View view) {
        if (gameMatrix[3][0] == 1) {
            button30.setText("Hold");
            button30.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button30.setText(String.valueOf(gameMatrix[3][0]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[3][0]);
        gameMatrix[3][0] -= 1;
        
    }

    public void Button31(View view) {
        if (gameMatrix[3][1] == 1) {
            button31.setText("Hold");
            button31.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button31.setText(String.valueOf(gameMatrix[3][1]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[3][1]);
        gameMatrix[3][1] -= 1;
        
    }

    public void Button32(View view) {
        if (gameMatrix[3][2] == 1) {
            button32.setText("Hold");
            button32.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button32.setText(String.valueOf(gameMatrix[3][2]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[3][2]);
        gameMatrix[3][2] -= 1;
        
    }

    public void Button33(View view) {
        if (gameMatrix[3][3] == 1) {
            button33.setText("Hold");
            button33.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button33.setText(String.valueOf(gameMatrix[3][3]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[3][3]);
        gameMatrix[3][3] -= 1;
        
    }

    public void Button40(View view) {
        if (gameMatrix[4][0] == 1) {
            button40.setText("Hold");
            button40.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button40.setText(String.valueOf(gameMatrix[4][0]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[4][0]);
        gameMatrix[4][0] -= 1;
        
    }

    public void Button41(View view) {
        if (gameMatrix[4][1] == 1) {
            button41.setText("Hold");
            button41.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button41.setText(String.valueOf(gameMatrix[4][1]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[4][1]);
        gameMatrix[4][1] -= 1;
        
    }

    public void Button42(View view) {
        if (gameMatrix[4][2] == 1) {
            button42.setText("Hold");
            button42.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button42.setText(String.valueOf(gameMatrix[4][2]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[4][2]);
        gameMatrix[4][2] -= 1;
        
    }

    public void Button43(View view) {
        if (gameMatrix[4][3] == 1) {
            button43.setText("Hold");
            button43.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button43.setText(String.valueOf(gameMatrix[4][3]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[4][3]);
        gameMatrix[4][3] -= 1;
        
    }

    public void Button50(View view) {
        if (gameMatrix[5][0] == 1) {
            button50.setText("Hold");
            button50.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button50.setText(String.valueOf(gameMatrix[5][0]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[5][0]);
        gameMatrix[5][0] -= 1;
        
    }

    public void Button51(View view) {
        if (gameMatrix[5][1] == 1) {
            button51.setText("Hold");
            button51.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button51.setText(String.valueOf(gameMatrix[5][1]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[5][1]);
        gameMatrix[5][1] -= 1;
        
    }

    public void Button52(View view) {
        if (gameMatrix[5][2] == 1) {
            button52.setText("Hold");
            button52.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button52.setText(String.valueOf(gameMatrix[5][2]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[5][2]);
        gameMatrix[5][2] -= 1;
        
    }

    public void Button53(View view) {
        if (gameMatrix[5][3] == 1) {
            button53.setText("Hold");
            button53.setOnTouchListener(listener);
            if (loaded_slap) slap.play(slapID, volume, volume, 1, 0, 1f);
            
        } else {
            button53.setText(String.valueOf(gameMatrix[5][3]));
            if(loaded_punch) punch.play(punchID,volume,volume,1,0,1f);
            
        }
        view.setAlpha(1f - 0.5f/gameMatrix[5][3]);
        gameMatrix[5][3] -= 1;
        
    }
}
