package trappedtildoom;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.openal.OpenALAudio;
import com.badlogic.gdx.backends.openal.OpenALSound;
import org.lwjgl.BufferUtils;
import trappedtildoom.core.extension.SoundEx;
import trappedtildoom.core.serialization.GameSlot;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ShortBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.openal.AL10.*;

public class DesktopSoundEx implements SoundEx {
    private static final Logger logger = Logger.getLogger(GameSlot.class.getName());

    public static final Field fieldBufferID;
    public final static Field fieldAudio;
    public final static Field fieldNoDevice;
    public final static Method methodObtainSource;

    static {
        Field tempFieldAudio = null;
        try {
            tempFieldAudio = OpenALSound.class.getDeclaredField("audio");
        } catch (NoSuchFieldException e) {
            logger.log(Level.SEVERE, null, e);
        }
        fieldAudio = tempFieldAudio;
        fieldAudio.setAccessible(true);

        Field tempFieldNoDevice = null;
        try {
            tempFieldNoDevice = OpenALAudio.class.getDeclaredField("noDevice");
        } catch (NoSuchFieldException e) {
            logger.log(Level.SEVERE, null, e);
        }
        fieldNoDevice = tempFieldNoDevice;
        fieldNoDevice.setAccessible(true);

        Field tempFieldBufferID = null;
        try {
            tempFieldBufferID = OpenALSound.class.getDeclaredField("bufferID");
        } catch (NoSuchFieldException e) {
            logger.log(Level.SEVERE, null, e);
        }
        fieldBufferID = tempFieldBufferID;
        fieldBufferID.setAccessible(true);

        Method tempMethodObtainSource = null;
        try {
            tempMethodObtainSource = OpenALAudio.class.getDeclaredMethod("obtainSource", boolean.class);
        } catch (NoSuchMethodException e) {
            logger.log(Level.SEVERE, null, e);
        }
        methodObtainSource = tempMethodObtainSource;
        methodObtainSource.setAccessible(true);
    }

    private final int bufferID;
    private final Sound sound;
    private final OpenALAudio audio;
    private final boolean noDevice;

    private int silenceBufferID = -1;

    public DesktopSoundEx(Sound sound) {
        this.sound = sound;

        OpenALAudio audio = null;
        try {
            audio = (OpenALAudio)fieldAudio.get(sound);
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, null, e);
        }
        this.audio = audio;

        boolean noDevice = false;
        try {
            noDevice = fieldNoDevice.getBoolean(audio);
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, null, e);
        }
        this.noDevice = noDevice;

        int bufferID = -1;
        try {
            bufferID = fieldBufferID.getInt(sound);
        } catch (IllegalAccessException e) {
            logger.log(Level.SEVERE, null, e);
        }
        this.bufferID = bufferID;

        silenceBufferID = alGenBuffers();
    }

    @Override
    public long play() {
        return sound.play();
    }

    @Override
    public long play(float volume) {
        return sound.play(volume);
    }

    @Override
    public long play(float volume, float pitch, float pan) {
        return sound.play(volume, pitch, pan);
    }

    @Override
    public long play(float volume, int numberOfTimes, double gap) {
        if (numberOfTimes == 1) {
            return sound.play(volume);
        }

        if (noDevice)
            return 0;

        int sourceID;
        try {
            sourceID = (int)methodObtainSource.invoke(audio, false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.log(Level.SEVERE, null, e);
            return -1;
        }

        if (sourceID == -1)
            return -1;

        if (gap > 0) {
            int sampleRate = 44100;
            ShortBuffer silenceBuffer = BufferUtils.createShortBuffer((int)(gap * 44100));
            alBufferData(silenceBufferID, AL_FORMAT_MONO16, silenceBuffer, sampleRate);
        }

        long soundId = audio.getSoundId(sourceID);
        alSourcei(sourceID, AL_LOOPING, AL_FALSE);
        alSourcef(sourceID, AL_GAIN, volume);
        for (int i = 0; i < numberOfTimes; i++) {
            alSourceQueueBuffers(sourceID, bufferID);
            if (gap > 0) {
                alSourceQueueBuffers(sourceID, silenceBufferID);
            }
        }
        alSourcePlay(sourceID);

        return soundId;
    }

    @Override
    public long loop() {
        return sound.loop();
    }

    @Override
    public long loop(float volume) {
        return sound.loop(volume);
    }

    @Override
    public long loop(float volume, float pitch, float pan) {
        return sound.loop(volume, pitch, pan);
    }

    @Override
    public void stop() {
        sound.stop();
    }

    @Override
    public void dispose() {
        sound.dispose();
        alDeleteBuffers(silenceBufferID);
    }

    @Override
    public void stop(long soundId) {
        sound.stop(soundId);
    }

    @Override
    public void setLooping(long soundId, boolean looping) {
        sound.setLooping(soundId, looping);
    }

    @Override
    public void setPitch(long soundId, float pitch) {
        sound.setPitch(soundId, pitch);
    }

    @Override
    public void setVolume(long soundId, float volume) {
        sound.setVolume(soundId, volume);
    }

    @Override
    public void setPan(long soundId, float pan, float volume) {
        sound.setPan(soundId, pan, volume);
    }

    @Override
    public void setPriority(long soundId, int priority) {
        sound.setPriority(soundId, priority);
    }
}
