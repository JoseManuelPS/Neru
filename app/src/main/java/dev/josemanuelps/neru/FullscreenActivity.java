package dev.josemanuelps.neru;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

public class FullscreenActivity extends AppCompatActivity {

    private TextView screen;
    private TextView screen_end;
    private TextView screen_top;
    private Thread thread;
    private Vibrator vibrator;

    private boolean running=false;
    private boolean stop_flag =false;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        screen_end = findViewById(R.id.screen_end);
        screen_top = findViewById(R.id.screen_top);

        screen = findViewById(R.id.screen);
        screen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running && stop_flag){
                    running = true;
                    stop_flag = false;
                    screen_end.setVisibility(View.VISIBLE);
                    screen_top.setVisibility(View.VISIBLE);
                    init();
                } else {
                    stop_flag = true;
                }
            }
        });
        screen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                thread.interrupt();
                running = false;
                screen.setText(getString(R.string.screen));
                screen_end.setVisibility(View.INVISIBLE);
                screen_top.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
    }

    public void init() {
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int mode = 0; mode <= 3; mode++) {
                        for (int time = selectTimeOut(mode); time > 0; time--) {
                            updateTV(mode, time);
                            Thread.sleep(1000);
                            if(time == 1){
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, 1));
                            }
                            if(mode == 3 && time == 1){
                                mode = 0;
                                count++;
                                screen_top.setText("Completados: " + count);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public int selectTimeOut(int mode) {

        int time;

        if (mode == 0) {
            time = 3;
        } else if (mode == 1) {
            time = 4;
        } else if (mode == 2) {
            time = 7;
        } else {
            time = 8;
        }

        return time;
    }

    public void updateTV(final int mode, final int time) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mode == 0) {
                    screen.setText("Comenzamos en: " + time);
                } else if (mode == 1) {
                    screen.setText("Inspiramos: " + time);
                } else if (mode == 2) {
                    screen.setText("Aguantamos: " + time);
                } else {
                    screen.setText("Expiramos: " + time);
                }
            }
        });
    }
}
