package trappedtildoom.core;

import com.badlogic.gdx.Game;
import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import trappedtildoom.core.screen.*;
import trappedtildoom.core.serialization.GameSlot;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.util.SettingsProperties;

import trappedtildoom.core.service.AbsoluteLogger;

public class TrappedTilDoomGame extends Game {

    public static final String version;

    private final SoundManager soundManager = new SoundManager();
    private final SettingsProperties properties;

    public BodyPodBeginningScreen bodyPodBeginningScreen;
    public LoadingScreen loadingScreen;
    public MainMenuScreen mainMenuScreen;
    public NewGameScreen newGameScreen;
    public OptionsScreen optionsScreen;
    public PauseScreen pauseScreen;
    public PlayGameScreen playGameScreen;
    public SelectGameScreen selectGameScreen;
    public SandboxScreen sandboxScreen;
    public GameSlot gameSlot;
    
    static {
        String packageVersion = TrappedTilDoomGame.class.getPackage().getImplementationVersion();
        version = packageVersion != null ? packageVersion : "dev";
    }

    public TrappedTilDoomGame(SettingsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void create() {
        AbsoluteLogger.info("[Game] Booting Game!");
        AbsoluteLogger.info("[Game] Initializing display!");
        AbsoluteLogger.info("[Game] Version is: " + version);

        applySettings();

        try {   
            bodyPodBeginningScreen = new BodyPodBeginningScreen(this);
        } catch (FileNotFoundException ex) {
            AbsoluteLogger.severe(null, ex);
        }

        loadingScreen = new LoadingScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        newGameScreen = new NewGameScreen(this);
        optionsScreen = new OptionsScreen(this);
        pauseScreen = new PauseScreen(this);
        playGameScreen = new PlayGameScreen(this);
        selectGameScreen = new SelectGameScreen(this);
        sandboxScreen = new SandboxScreen(this);

        //setScreen(mainMenuScreen);
        //setScreen(loadingScreen);
        setScreen(sandboxScreen);
    }

    @Override
    public void dispose() {
        soundManager.dispose();
        bodyPodBeginningScreen.dispose();
        mainMenuScreen.dispose();
        newGameScreen.dispose();
        optionsScreen.dispose();
        pauseScreen.dispose();
        playGameScreen.dispose();
        selectGameScreen.dispose();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public SettingsProperties getProperties() {
        return properties;
    }

    public void applySettings() {
        float volumeValue = properties.getInteger(SettingsProperties.Property.SOUNDVOLUME, SettingsProperties.MAX_VOLUME);
        soundManager.setEnabled(volumeValue > SettingsProperties.MIN_VOLUME);
        soundManager.setVolume((volumeValue - SettingsProperties.MIN_VOLUME) / (SettingsProperties.MAX_VOLUME - SettingsProperties.MIN_VOLUME));
    }
}
