package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.input.MainMenuInputProcessor;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.util.SettingsProperties;

public class OptionsScreen extends TrappedTilDoomScreen {
    
    private Stage stage = new Stage();
    private Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));
    private InputMultiplexer inputMultiplexer;

    public OptionsScreen(TrappedTilDoomGame trappedTilDoomGame) {
        super(trappedTilDoomGame);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new MainMenuInputProcessor(game));

        boolean fullScreen = game.getProperties().getBoolean(SettingsProperties.Property.FULLSCREEN, true);
        boolean vSyncEnabled = game.getProperties().getBoolean(SettingsProperties.Property.VSYNCENABLED, true);
        int volumeLevel = game.getProperties().getInteger(SettingsProperties.Property.SOUNDVOLUME, 100);

        final TextButton fullScreenButton = new TextButton(fullScreen ? "Fullscreen" : "Windowed", skin);
        fullScreenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);

                CharSequence currentText = fullScreenButton.getText();
                boolean isFullscreen = "Fullscreen".contentEquals(currentText);

                fullScreenButton.setText(isFullscreen ? "Windowed" : "Fullscreen");
                Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), !isFullscreen);

                game.getProperties().setProperty(SettingsProperties.Property.FULLSCREEN, !isFullscreen);
            }
        });

        final TextButton vSyncButton = new TextButton(vSyncEnabled ? "Use vSync" : "Don`t use vSync", skin);
        vSyncButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);

                CharSequence currentText = vSyncButton.getText();
                boolean isvSync = "Use vSync".contentEquals(currentText);

                vSyncButton.setText(isvSync ? "Don`t use vSync" : "Use vSync");
                Gdx.graphics.setVSync(!isvSync);

                game.getProperties().setProperty(SettingsProperties.Property.VSYNCENABLED, !isvSync);
            }
        });

        final Slider volumeSlider = new Slider(SettingsProperties.MIN_VOLUME, SettingsProperties.MAX_VOLUME, 10, false, skin);
        volumeSlider.setValue(volumeLevel);
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                float volumeValue = volumeSlider.getValue();
                getSoundManager().setEnabled(volumeValue > SettingsProperties.MIN_VOLUME);
                getSoundManager().setVolume((volumeSlider.getValue() - volumeSlider.getMinValue()) / (volumeSlider.getMaxValue() - volumeSlider.getMinValue()));
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);

                game.getProperties().setProperty(SettingsProperties.Property.SOUNDVOLUME, (int)volumeValue);
            }
        });

        final TextButton doneButton = new TextButton("Done", skin);
        doneButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.mainMenuScreen);
            }

        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(fullScreenButton);
        table.row();
        table.add(vSyncButton);
        table.row();
        table.add(volumeSlider);
        table.row();
        table.add(doneButton);
        table.layout();

        stage.addActor(table);

    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height, true);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    protected void update(float delta) {
        stage.act(delta);
    }

    @Override
    protected void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        skin.dispose();
    }
}
