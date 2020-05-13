package com.example.news;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    private String MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1500);

                    Intent intent = new Intent(SplashScreen.this,RegisterMain.class);
                    startActivity(intent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }

        });thread.start();
    }
}
