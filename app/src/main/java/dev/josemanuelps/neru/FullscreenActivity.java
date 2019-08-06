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
    private boolean running=false;
    private Thread thread;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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
                if(running){
                    thread.interrupt();
                    running = false;
                    screen.setText(getString(R.string.screen));
                }else{
                    running = true;
                    init();
                }
            }
        });

        vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
    }

    public void init() {
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int x = 0; x < 4; x++) {
                        for (int y = selectTimeOut(x); y > 0; y--) {
                            updateTV(x, y);
                            Thread.sleep(1000);
                            if(y==1){
                                vibrator.vibrate(VibrationEffect.createOneShot(1000, 1));
                            }
                            if(x==3 && y == 1){
                                x=0;
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

    public int selectTimeOut(int x) {

        int aux;

        if (x == 0) {
            aux = 3;
        } else if (x == 1) {
            aux = 4;
        } else if (x == 2) {
            aux = 7;
        } else {
            aux = 8;
        }

        return aux;
    }

    public void updateTV(final int x, final int y) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (x == 0) {
                    screen.setText("Comenzamos en: " + y);
                } else if (x == 1) {
                    screen.setText("Inspiramos: " + y);
                } else if (x == 2) {
                    screen.setText("Aguantamos: " + y);
                } else {
                    screen.setText("Expiramos: " + y);
                }
            }
        });
    }
}
