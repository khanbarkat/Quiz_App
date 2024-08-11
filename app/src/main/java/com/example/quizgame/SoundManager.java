package com.example.quizgame;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private Context context;
    private MediaPlayer mediaPlayer;

    public SoundManager(Context context) {
        this.context = context;
    }

    public void playSound(int resourceId) {
        // Release any existing MediaPlayer instance
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        // Create a new MediaPlayer instance for the specified sound resource
        mediaPlayer = MediaPlayer.create(context, resourceId);

        // Start playing the sound
        mediaPlayer.start();

        // Release the MediaPlayer resources when the sound playback is complete
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

