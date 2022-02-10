package com.example.boundservice30112021;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class PlayMp3Service extends Service {

    int REQUEST_CODE_PAUSE = 1;
    int REQUEST_CODE_PLAY = 0;
    NotificationManager mNotificationManager;
    Notification mNotification;
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mediaPlayer = MediaPlayer.create(this,R.raw.nhac);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mNotification = createNotification("Bậc đế vương","Đang phát" , REQUEST_CODE_PAUSE);
                startForeground(1,mNotification);
            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Notification createNotification(String title , String  message, int requestCode){
        Intent intentPlay = new Intent();

        PendingIntent pendingIntentPlay = PendingIntent.getService(this,REQUEST_CODE_PLAY,intentPlay,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPause = new Intent();

        PendingIntent pendingIntentPause = PendingIntent.getService(this,REQUEST_CODE_PAUSE,intentPause,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,"My Channel");
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle(title);
        notification.setContentText(message);

        if (requestCode == REQUEST_CODE_PLAY){
            notification.addAction(android.R.drawable.ic_media_play,"Play",pendingIntentPlay);
        }else{
            notification.addAction(android.R.drawable.ic_media_pause,"Pause",pendingIntentPause);
        }
        return notification.build();
    }
}
