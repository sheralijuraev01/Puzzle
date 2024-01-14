package uz.sher.puzzle15.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import uz.sher.puzzle15.R;

public class MySoundPool {
    private SoundPool soundPool;
    private int gameOver, clickButton, clickStone, soundWin;


    public MySoundPool(Context context) {

        initSoundPool(context);
    }

    private void initSoundPool(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setMaxStreams(4).setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }

        gameOver = soundPool.load(context, R.raw.game_win, 1);
        clickButton = soundPool.load(context, R.raw.click_sound, 1);
        clickStone = soundPool.load(context, R.raw.click_item, 1);

        soundWin = soundPool.load(context, R.raw.game_winner, 1);
    }


    public void playGameOver() {
        soundPool.play(gameOver, 1, 1, 0, 0, 1);
    }

    public void playClickStone() {
        soundPool.play(clickStone, 1, 1, 0, 0, 1);
    }

    public void playClickButton() {
        soundPool.play(clickButton, 1, 1, 0, 0, 1);
    }

    public void playSoundWin() {
        soundPool.play(soundWin, 1, 1, 0, 0, 1);
    }


    public void soundPoolRelease() {
        soundPool.release();
    }
}
