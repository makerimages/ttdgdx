package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.serialization.GameSlot;
import trappedtildoom.core.service.SoundManager;

public class NewGameScreen extends TrappedTilDoomScreen {
    
    private Stage stage = new Stage();
    private Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));
    private final TextField newGameName;

    public NewGameScreen(TrappedTilDoomGame trappedTilDoomGame) {
        super(trappedTilDoomGame);

        newGameName = new TextField("", skin);

        final TextButton saveGameButton = new TextButton("Save Game", skin);
        saveGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String fileName = newGameName.getText();
                if (!fileName.isEmpty()) {
                    getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                    game.gameSlot = new GameSlot(fileName);
                    if (!game.gameSlot.validate()) {
                        game.setScreen(game.bodyPodBeginningScreen);
                        return;
                    }

                    Dialog dialog = new Dialog("Oh no", skin, "dialog") {
                        protected void result (Object object) {
                            if (object == 1) {
                                game.gameSlot.delete();
                                game.setScreen(game.bodyPodBeginningScreen);
                            } else if (object == 0) {
                                game.setScreen(game.playGameScreen);
                            }
                        }
                    };

                    dialog.text("Another game with the same name already exists.\nWhat we should do?")
                            .button("Change Name", -1)
                            .button("Load Existing", 0)
                            .button("Overwrite", 1)
                            .key(Input.Keys.O, 1)
                            .key(Input.Keys.L, 0)
                            .key(Input.Keys.ESCAPE, -1)
                            .key(Input.Keys.C, -1)
                            .show(stage);
                }
            }
        });

        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.mainMenuScreen);
            }
        });

        final Table table = new Table();
        table.setFillParent(true);

        table.add(newGameName);
        table.row();
        table.add(saveGameButton);
        table.row();
        table.add(backButton);
        table.layout();

        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height, true);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        newGameName.setText("Name");
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
