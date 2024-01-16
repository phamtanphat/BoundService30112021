package com.example.boundservice30112021;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class PlayMp3Service extends Service {

    int REQUEST_CODE_PAUSE = 1;
    int REQUEST_CODE_PLAY = 0;
    NotificationManager mNotificationManager;
    Notification mNotification;
    MediaPlayer mediaPlayer;

    public class PlayMp3Binder extends Binder {
        public PlayMp3Service getService() {
            return PlayMp3Service.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayMp3Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.nhac);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mNotification = createNotification("Bậc đế vương", "Đang phát", REQUEST_CODE_PAUSE);
                startForeground(1, mNotification);
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int requestCode = intent.getIntExtra("requestCode", -1);
            if (requestCode >= 0) {
                if (requestCode == REQUEST_CODE_PAUSE) {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        mNotification = createNotification("Bậc đế vương", "Tạm dừng", REQUEST_CODE_PLAY);
                    }
                }
                if (requestCode == REQUEST_CODE_PLAY) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                        mNotification = createNotification("Bậc đế vương", "Đang phát", REQUEST_CODE_PAUSE);
                    }
                }
                mNotificationManager.notify(1, mNotification);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @SuppressLint("LaunchActivityFromNotification")
    private Notification createNotification(String title, String message, int requestCode) {
        Intent intentOpenApp = new Intent(this, MainActivity.class);
        intentOpenApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingOpenApp = PendingIntent.getActivity(this, Integer.MIN_VALUE, intentOpenApp, PendingIntent.FLAG_IMMUTABLE);

        Intent intentPlay = new Intent(this, PlayMp3Service.class);
        intentPlay.putExtra("requestCode", REQUEST_CODE_PLAY);

        PendingIntent pendingIntentPlay = PendingIntent.getService(this, REQUEST_CODE_PLAY, intentPlay, PendingIntent.FLAG_IMMUTABLE);

        Intent intentPause = new Intent(this, PlayMp3Service.class);
        intentPause.putExtra("requestCode", REQUEST_CODE_PAUSE);

        PendingIntent pendingIntentPause = PendingIntent.getService(this, REQUEST_CODE_PAUSE, intentPause, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "My Channel");
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle(title);
        notification.setContentText(message);
        notification.setContentIntent(pendingOpenApp);

        if (requestCode == REQUEST_CODE_PLAY) {
            notification.addAction(android.R.drawable.ic_media_play, "Play", pendingIntentPlay);
        } else {
            notification.addAction(android.R.drawable.ic_media_pause, "Pause", pendingIntentPause);
        }
        return notification.build();
    }
}
