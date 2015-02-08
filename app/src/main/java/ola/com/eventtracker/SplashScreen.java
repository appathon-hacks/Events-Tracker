package ola.com.eventtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    private Handler handler = new Handler();

    //Constants
    private static int SPLASH_TIMEOUT= 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getActionBar().hide();

        startMainScreenWithDelay();

    }

    private void startMainScreenWithDelay() {


        Runnable startScreenRunnable = new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };

        handler.postDelayed(startScreenRunnable,SPLASH_TIMEOUT);

    }
}