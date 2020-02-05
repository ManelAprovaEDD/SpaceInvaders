package com.example.spaceinvaders;

import android.media.MediaPlayer;
import android.os.Bundle;

public class SpaceInvadersActivity extends MainMenu {
    public SpaceInvadersSurfaceView mySpaceInvadersSurfaceView;
    private MediaPlayer mediaPlayer;
    static MediaPlayer soDispar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySpaceInvadersSurfaceView = new SpaceInvadersSurfaceView(this);
        setContentView(mySpaceInvadersSurfaceView);
        soDispar = MediaPlayer.create(getApplicationContext(),R.raw.so_dispar);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.so_space);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void onPause(){
        super.onPause();
        mySpaceInvadersSurfaceView.stopThread();
        mediaPlayer.stop();
        mediaPlayer.release();
        soDispar.stop();
        soDispar.release();
    }

    public MediaPlayer getSoDispar() {
        return soDispar;
    }
}
