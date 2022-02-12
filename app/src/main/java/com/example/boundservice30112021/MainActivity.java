package com.example.boundservice30112021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mBtnStartMp3,mBtnStopMp3;
    TextView mTvStart,mTvEnd;
    SeekBar mSkMp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStartMp3 = findViewById(R.id.buttonStartMp3);
        mBtnStopMp3 = findViewById(R.id.buttonStopMp3);
        mTvStart = findViewById(R.id.buttonStartMp3);
        mTvEnd = findViewById(R.id.textEndTime);
        mSkMp3 = findViewById(R.id.seekbarMp3);

        mBtnStartMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlayMp3Service.class);
                startService(intent);
            }
        });
        mBtnStopMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlayMp3Service.class);
                stopService(intent);
            }
        });

        Log.d("BBB","OnCreate");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("BBB","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("BBB","onDestroy");
    }
}