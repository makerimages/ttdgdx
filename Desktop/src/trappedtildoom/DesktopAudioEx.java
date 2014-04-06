package trappedtildoom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import trappedtildoom.core.extension.AudioEx;
import trappedtildoom.core.extension.SoundEx;

public class DesktopAudioEx implements AudioEx {
    @Override
    public SoundEx newSound(FileHandle file) {
        return new DesktopSoundEx(Gdx.audio.newSound(file));
    }
}
