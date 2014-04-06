package trappedtildoom;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.extension.GdxEx;
import trappedtildoom.core.util.SettingsProperties;

public class DesktopStarter {
    public static void main(String[] args) {
        SettingsProperties properties = new SettingsProperties();

        Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();

        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.fullscreen = properties.getBoolean(SettingsProperties.Property.FULLSCREEN, true);
        configuration.title = "Trapped `til Doom v. " + TrappedTilDoomGame.version;
        configuration.useGL20 = true;
        configuration.useCPUSynch=false;

        configuration.vSyncEnabled = properties.getBoolean(SettingsProperties.Property.VSYNCENABLED, false);
        configuration.depth = displayMode.bitsPerPixel;
        configuration.width = displayMode.width;
        configuration.height = displayMode.height;
        
        GdxEx.audio = new DesktopAudioEx();

        new LwjglApplication(new TrappedTilDoomGame(properties), configuration);
    }
}
