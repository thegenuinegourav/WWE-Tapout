package thegenuinegourav.wwetapout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import static thegenuinegourav.wwetapout.R.drawable.aj;
import static thegenuinegourav.wwetapout.R.drawable.bayley;
import static thegenuinegourav.wwetapout.R.drawable.braun;
import static thegenuinegourav.wwetapout.R.drawable.charlotte;

public class SelectPlayersActivity extends Activity {
    private ImageView select_1p, select_2p;
    private Button select_status, select_1p_button, select_2p_button;
    private SharedPreferences prefs = null;
    private boolean isSelect2p, isSelect1p;
    private String select_1p_string, select_2p_string;
    private int select_1p_power, select_2p_power;
    private MediaPlayer mMediaPlayer;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private String[] urls, file_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(20);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    public void showDialogBox(int i) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.faq_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.faq_dialog_text);
        text.setText(i+" taps & 1 hold require to break these superstars!");
        Button dialogButton = (Button) dialog.findViewById(R.id.faq_dialog_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void FAQ7(View view) {
        showDialogBox(7);
    }
    public void FAQ5(View view) {
        showDialogBox(5);
    }
    public void FAQ4(View view) {
        showDialogBox(4);
    }
    public void FAQ3(View view) {
        showDialogBox(3);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        int noOfURLs, counter = 0;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... url) {
            noOfURLs = url.length;
            for (String u : urls) {
                downloadFile(u, file_names[counter]);
                counter++;
            }
            return null;
        }

        public void downloadFile(String urlString, String file_name) {
            int count = 0;
            URL url;
            try {
                url = new URL(urlString);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/" + file_name);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(counter);
            switch (counter) {
                case 0:
                case 1:
                case 2: pDialog.setMessage("Downloading files...");
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7: pDialog.setMessage("This is One Time Download");
                    break;
                case 8:
                case 9:
                case 10:
                case 11: pDialog.setMessage("Please Wait...");
                    break;
                case 12:
                case 13:
                case 14:
                case 15: pDialog.setMessage("This is One Time Download");
                    break;
                case 16:
                case 17:
                case 18: pDialog.setMessage("Getting things ready...");
                default: pDialog.setMessage("Loading...");
            }

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            loadActivity();
            prefs.edit().putBoolean("firstrun", false).commit();
        }

    }

    public void loadActivity() {
        setContentView(R.layout.activity_select_players);

        prefs = getSharedPreferences("thegenuinegourav.wwetapout", MODE_PRIVATE);

        select_1p = (ImageView) findViewById(R.id.select_1);
        select_2p = (ImageView) findViewById(R.id.select_2);
        select_status = (Button) findViewById(R.id.selection_status);
        select_1p_button = (Button) findViewById(R.id.select_1p_button);
        select_2p_button = (Button) findViewById(R.id.select_2p_button);

        select_1p_button.setVisibility(View.VISIBLE);
        select_1p_button.setClickable(true);
        select_2p_button.setClickable(false);
        select_2p_button.setVisibility(View.INVISIBLE);

        select_1p.setAlpha(1f);
        select_2p.setAlpha(1f);
        select_status.setText("Select 1p");
        select_status.setBackgroundColor(Color.BLACK);

        isSelect2p = isSelect1p = false;
        select_1p_string = "bray";
        select_2p_string = "becky";
        select_2p_power = select_1p_power = 3;

        YoYo.with(Techniques.Flash)
                .duration(3500)
                .repeat(Integer.MAX_VALUE)
                .playOn(select_status);
    }

    public void startBackgroundMusic() {
        mMediaPlayer = MediaPlayer.create(SelectPlayersActivity.this, R.raw.raw_theme);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            setContentView(R.layout.first_time_layout);
            urls = new String[]{"https://drive.google.com/uc?id=0B8dKT6z4lKMqS2ZTQm1Vd0g4MXc&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqYWFjeXBrNEpyaFU&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqc2t2WlI5UDVzVlU&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqQ05SWEctdERyME0&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqUGVHX2o3akpMLUk&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqUnUyUVRRcUlVaVk&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqcklKMWQxcUFUeXM&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqdW12Z0JsT1E0MzA&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqX3c4dkQtaXFrQnc&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqNm5NeVdhS0hwV3M&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqNTJodF9lcjlMd0U&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqYUptYTZ6U1hqOGc&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqR1hRczBNWDlKVm8&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqMTlPTkdYZjZSYjA&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqMHFEb0NuOEVkUlk&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqQWF0aDlvR09NTjg&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqb3VvZFh6Uzgwd1k&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqc2IzdmQxY0tkcGM&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqN1RDZTVqeUZPMWc&export=download",
                    "https://drive.google.com/uc?id=0B8dKT6z4lKMqSkxSbHdqUElFN0E&export=download"
            };
            file_names = new String[]{"aj.wmv", "asuka.wmv", "bayley.wmv", "becky.wmv", "braun.wmv", "bray.wmv", "brock.wmv",
                    "charlotte.wmv", "dean.wmv", "finn.wmv", "kevin.wmv", "roman.wmv", "samoa.wmv", "sasha.wmv", "seth.wmv", "shinsuke.wmv",
                    "undertaker.wmv", "alexa.wmv", "john.wmv", "big_show.wmv"};
            new DownloadFileFromURL().execute(urls);
            YoYo.with(Techniques.Flash)
                    .duration(7500)
                    .repeat(Integer.MAX_VALUE)
                    .playOn(findViewById(R.id.wwe_logo));
            YoYo.with(Techniques.Flash)
                    .duration(7500)
                    .repeat(Integer.MAX_VALUE)
                    .playOn(findViewById(R.id.wwe_logo_2));
        } else {
            loadActivity();
        }
        startBackgroundMusic();

    }

    @Override
    protected void onPause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        super.onPause();
    }

    public void Select_one_p(View view) {
        select_status.setText("Select 2P");
        isSelect1p = true;
        select_1p.setAlpha(0.5f);
        select_1p_button.setVisibility(View.INVISIBLE);
        select_1p_button.setClickable(false);
        select_2p_button.setVisibility(View.VISIBLE);
        select_2p_button.setClickable(true);
    }

    @Override
    public void onBackPressed() {
        if (isSelect2p && isSelect1p) {
            select_status.setText("Select 2p");
            select_status.setBackgroundColor(Color.BLACK);
            select_2p_button.setVisibility(View.VISIBLE);
            select_2p_button.setClickable(true);
            select_2p.setAlpha(1f);
            isSelect2p = false;
        } else if (isSelect1p) {
            select_status.setText("Select 1p");
            select_status.setBackgroundColor(Color.BLACK);
            select_1p.setAlpha(1f);
            isSelect1p = false;
            select_1p_button.setVisibility(View.VISIBLE);
            select_1p_button.setClickable(true);
            select_2p_button.setVisibility(View.INVISIBLE);
            select_2p_button.setClickable(false);
        } else {
            super.onBackPressed();
        }
    }

    public void Select_two_p(View view) {
        if (!select_1p_string.equals(select_2p_string)) {
            isSelect2p = true;
            select_2p.setAlpha(0.5f);
            select_status.setText("FIGHT");
            select_status.setBackgroundColor(Color.RED);
            select_2p_button.setVisibility(View.INVISIBLE);
            select_2p_button.setClickable(false);
            select_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SelectPlayersActivity.this, EntranceActivity.class);
                    i.putExtra("one_p_name", select_1p_string);
                    i.putExtra("one_p_power", select_1p_power);
                    i.putExtra("two_p_name", select_2p_string);
                    i.putExtra("two_p_power", select_2p_power);
                    startActivity(i);
                }
            });
        } else {
            Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
        }

    }

    public void Bray(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.bray);
            select_1p_string = "bray";
            select_1p_power = 3;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("bray")) {
                select_2p.setImageResource(R.drawable.bray);
                select_2p_string = "bray";
                select_2p_power = 3;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Becky(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.becky);
            select_1p_string = "becky";
            select_1p_power = 3;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("becky")) {
                select_2p.setImageResource(R.drawable.becky);
                select_2p_string = "becky";
                select_2p_power = 3;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Dean(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.dean);
            select_1p_string = "dean";
            select_1p_power = 3;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("dean")) {
                select_2p.setImageResource(R.drawable.dean);
                select_2p_string = "dean";
                select_2p_power = 3;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void BigShow(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.big_show);
            select_1p_string = "big_show";
            select_1p_power = 3;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("big_show")) {
                select_2p.setImageResource(R.drawable.big_show);
                select_2p_string = "big_show";
                select_2p_power = 3;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Bayley(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(bayley);
            select_1p_string = "bayley";
            select_1p_power = 3;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("bayley")) {
                select_2p.setImageResource(bayley);
                select_2p_string = "bayley";
                select_2p_power = 3;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void John(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.john);
            select_1p_string = "john";
            select_1p_power = 4;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("john")) {
                select_2p.setImageResource(R.drawable.john);
                select_2p_string = "john";
                select_2p_power = 4;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Samoa(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.samoa);
            select_1p_string = "samoa";
            select_1p_power = 4;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("samoa")) {
                select_2p.setImageResource(R.drawable.samoa);
                select_2p_string = "samoa";
                select_2p_power = 4;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Alexa(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.alexa);
            select_1p_string = "alexa";
            select_1p_power = 4;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("alexa")) {
                select_2p.setImageResource(R.drawable.alexa);
                select_2p_string = "alexa";
                select_2p_power = 4;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Shinsuke(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.shinsuke);
            select_1p_string = "shinsuke";
            select_1p_power = 4;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("shinsuke")) {
                select_2p.setImageResource(R.drawable.shinsuke);
                select_2p_string = "shinsuke";
                select_2p_power = 4;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Finn(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.finn);
            select_1p_string = "finn";
            select_1p_power = 4;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("finn")) {
                select_2p.setImageResource(R.drawable.finn);
                select_2p_string = "finn";
                select_2p_power = 4;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Sasha(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.sasha);
            select_1p_string = "sasha";
            select_1p_power = 5;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("sasha")) {
                select_2p.setImageResource(R.drawable.sasha);
                select_2p_string = "sasha";
                select_2p_power = 5;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Aj(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(aj);
            select_1p_string = "aj";
            select_1p_power = 5;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("aj")) {
                select_2p.setImageResource(aj);
                select_2p_string = "aj";
                select_2p_power = 5;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Kevin(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.kevin);
            select_1p_string = "kevin";
            select_1p_power = 5;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("kevin")) {
                select_2p.setImageResource(R.drawable.kevin);
                select_2p_string = "kevin";
                select_2p_power = 5;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Roman(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.roman);
            select_1p_string = "roman";
            select_1p_power = 5;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("roman")) {
                select_2p.setImageResource(R.drawable.roman);
                select_2p_string = "roman";
                select_2p_power = 5;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Undertaker(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.undertaker);
            select_1p_string = "undertaker";
            select_1p_power = 5;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("undertaker")) {
                select_2p.setImageResource(R.drawable.undertaker);
                select_2p_string = "undertaker";
                select_2p_power = 5;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Seth(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.seth);
            select_1p_string = "seth";
            select_1p_power = 7;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("seth")) {
                select_2p.setImageResource(R.drawable.seth);
                select_2p_string = "seth";
                select_2p_power = 7;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Brock(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.brock);
            select_1p_string = "brock";
            select_1p_power = 7;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("brock")) {
                select_2p.setImageResource(R.drawable.brock);
                select_2p_string = "brock";
                select_2p_power = 7;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Charlotte(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(charlotte);
            select_1p_string = "charlotte";
            select_1p_power = 7;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("charlotte")) {
                select_2p.setImageResource(charlotte);
                select_2p_string = "charlotte";
                select_2p_power = 7;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Asuka(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(R.drawable.asuka);
            select_1p_string = "asuka";
            select_1p_power = 7;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("asuka")) {
                select_2p.setImageResource(R.drawable.asuka);
                select_2p_string = "asuka";
                select_2p_power = 7;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }

    public void Braun(View view) {
        if (!isSelect1p) {
            select_1p.setImageResource(braun);
            select_1p_string = "braun";
            select_1p_power = 7;
        } else if (!isSelect2p) {
            if (!select_1p_string.equals("braun")) {
                select_2p.setImageResource(braun);
                select_2p_string = "braun";
                select_2p_power = 7;
            } else {
                Toast.makeText(this, "Select Different Player", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Press Back Button to change players", Toast.LENGTH_SHORT).show();
        }
    }
}
