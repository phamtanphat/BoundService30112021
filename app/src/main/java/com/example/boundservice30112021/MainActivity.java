package com.example.boundservice30112021;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button mBtnStartMp3,mBtnStopMp3;
    TextView mTvStart,mTvEnd;
    SeekBar mSkMp3;
    String currentTime,endTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStartMp3 = findViewById(R.id.buttonStartMp3);
        mBtnStopMp3 = findViewById(R.id.buttonStopMp3);
        mTvStart = findViewById(R.id.textStartTime);
        mTvEnd = findViewById(R.id.textEndTime);
        mSkMp3 = findViewById(R.id.seekbarMp3);

        mBtnStartMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlayMp3Service.class);
                startService(intent);
                bindService(intent,connection,BIND_AUTO_CREATE);
            }
        });
        mBtnStopMp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlayMp3Service.class);
                stopService(intent);
                unbindService(connection);
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayMp3Service.PlayMp3Binder binder = (PlayMp3Service.PlayMp3Binder) service;
            PlayMp3Service playMp3Service = binder.getService();
            mSkMp3.setMax(playMp3Service.getMediaPlayer().getDuration());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (playMp3Service.getMediaPlayer() != null){
                        mTvStart.setText(convertTime(playMp3Service.getMediaPlayer().getCurrentPosition()));
                        mSkMp3.setProgress(playMp3Service.getMediaPlayer().getCurrentPosition());
                        mTvEnd.setText(convertTime(playMp3Service.getMediaPlayer().getDuration()));
                        new Handler().postDelayed(this,1000);
                    }

                }
            },1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private String convertTime(long time){
        int seconds = (int) ((time / 1000) % 60);
        int minus = (int) (time / 60000);

        String textSeconds = "";
        String textMinus = "";
        if (seconds < 10){
            textSeconds = "0"+ seconds;
        }else{
            textSeconds = String.valueOf(seconds);
        }
        if (minus < 10){
            textMinus = "0"+ minus;
        }else{
            textMinus = String.valueOf(minus);
        }
        return textMinus + ":"+textSeconds;
    }
}