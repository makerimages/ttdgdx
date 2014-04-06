package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.service.SoundManager;

public class PauseScreen extends TrappedTilDoomScreen {
    
    private Stage stage = new Stage();
    private Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));
    
    private InputMultiplexer inputMultiplexer;
    
    public PauseScreen(TrappedTilDoomGame trappedTilDoomGame) {
        super(trappedTilDoomGame);
        
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(commonInputProcessor);
        inputMultiplexer.addProcessor(new PauseScreenInputProcessor());

        TextButton continueGameButton = new TextButton("Continue", skin);
        continueGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.playGameScreen);
            }
        });

        TextButton saveGameButton = new TextButton("Save Game", skin);
        saveGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.gameSlot.save(game.playGameScreen.getData());

                Dialog dialog = new Dialog("Save Game", skin, "dialog") {
                    @Override
                    protected void result (Object object) { }
                };

                dialog.text("Game save successful!")
                        .button("OK")
                        .key(Keys.ENTER, null)
                        .key(Keys.ESCAPE, null)
                        .show(stage);
            }
        });

        TextButton optionsButton = new TextButton("Options", skin);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.optionsScreen);
            }
        });

        TextButton quitGameButton = new TextButton("Quit to main menu", skin);
        quitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);

                Dialog dialog = new Dialog("Quit to main menu", skin, "dialog") {
                    @Override
                    protected void result (Object object) {
                        if ((int)object >= 0) {
                            if (object == 1) {
                                game.gameSlot.save(game.playGameScreen.getData());
                                game.gameSlot = null;
                            }
                            game.setScreen(game.mainMenuScreen);
                        }
                    }
                };

                dialog.text("Do you want to save the game before quitting to main menu?")
                        .button("Yes", 1)
                        .button("No", 0)
                        .button("Cancel", -1)
                        .key(Keys.Y, 1)
                        .key(Keys.N, 0)
                        .key(Keys.ESCAPE, -1)
                        .show(stage);
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        table.add(continueGameButton);
        table.row();
        table.add(saveGameButton);
        table.row();
        table.add(optionsButton);
        table.row();
        table.add(quitGameButton);
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

    private class PauseScreenInputProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int key) {
            if (key == Keys.ESCAPE) {
                game.setScreen(game.playGameScreen);
                return true;
            }
            return false;
        }
    }
}
