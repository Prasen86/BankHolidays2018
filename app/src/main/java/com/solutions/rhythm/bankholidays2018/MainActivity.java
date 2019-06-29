package com.solutions.rhythm.bankholidays2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread timerThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                }catch (Exception e) {
                    Log.i("Error", e.getMessage());
                }finally {
                    Intent intent = new Intent(MainActivity.this,HolidayPage.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
