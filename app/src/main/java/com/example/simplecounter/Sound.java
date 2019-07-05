package com.example.simplecounter;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {

    private Context appContext;
    private MediaPlayer mediaPlayer;

    public Sound(Context appContext){
        this.appContext = appContext;
    }

    //up click sound method
    protected void playUpClick() {
        mediaPlayer = MediaPlayer.create(appContext, R.raw.upclick);
        mediaPlayer.start();
        releasePlayer(mediaPlayer);
    }

    //down click sound method
    protected void playDownClick() {
        mediaPlayer = MediaPlayer.create(appContext, R.raw.downclick);
        mediaPlayer.start();
        releasePlayer(mediaPlayer);
    }

    //reset click sound method
    protected void playResetClick() {
        mediaPlayer = MediaPlayer.create(appContext, R.raw.reset);
        mediaPlayer.start();
        releasePlayer(mediaPlayer);
    }

    private void releasePlayer(MediaPlayer player){
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.release();
            }
        });
    }
}
