package trappedtildoom.core.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.core.extension.GdxEx;
import trappedtildoom.core.extension.SoundEx;
import trappedtildoom.core.util.LRUCache;

public class SoundManager implements LRUCache.CacheEntryRemovedListener<SoundManager.TrappedTilDoomSound, SoundEx>, Disposable {
    public enum TrappedTilDoomSound {
        CLICK("res/sfx/click1.wav"),
        XP("res/sfx/xp.wav"),
        HURT("res/sfx/hurt.wav"),
        FOOD_LOWER("res/sfx/FoodLower.wav"),
        THUNDER("res/sfx/thunder.wav");
        
        private final String fileName;

        private TrappedTilDoomSound(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    private boolean enabled = true;
    private float volume = 1.0f;
    private final LRUCache<TrappedTilDoomSound, SoundEx> soundCache;

    public SoundManager() {
        soundCache = new LRUCache<>(10);
        soundCache.setEntryRemovedListener(this);
    }

    public void play(TrappedTilDoomSound sound) {
        play(sound, 1, 0);
    }

    public void play(TrappedTilDoomSound sound, int numberOfTimes, double gap) {
        if (!enabled) {
            return;
        }

        SoundEx soundToPlay = soundCache.get(sound);
        if (soundToPlay == null) {
            FileHandle soundFile = Gdx.files.internal(sound.getFileName());
            soundToPlay = GdxEx.audio.newSound(soundFile);
            soundCache.add(sound, soundToPlay);
        }

        soundToPlay.play(volume, numberOfTimes, gap);
    }

    public void setVolume(float volume) {
        this.volume = Math.max(0, Math.min(1, volume));
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void notifyEntryRemoved(TrappedTilDoomSound key, SoundEx value) {
        value.dispose();
    }

    @Override
    public void dispose() {
        for (SoundEx sound : soundCache.retrieveAll()) {
            sound.stop();
            sound.dispose();
        }
    }
}
