package trappedtildoom.core.extension;

import com.badlogic.gdx.audio.Sound;

public interface SoundEx extends Sound {
    long play(float volume, int numberOfTimes, double gap);
}
