package com.yeehungchong.p10_gettingmylocationsenhanced;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

public class MusicService extends Service {

    private MediaPlayer player = new MediaPlayer();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder", "music.mp3");
            player.setDataSource(file.getPath());
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.setLooping(true);
        player.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}