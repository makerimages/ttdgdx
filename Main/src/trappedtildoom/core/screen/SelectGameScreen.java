package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import trappedtildoom.core.serialization.GameSlot;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.input.SelectGameInputProcessor;
import trappedtildoom.core.service.SoundManager;

public class SelectGameScreen extends TrappedTilDoomScreen {
    
    private final Stage stage = new Stage();
    private final Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));
    private final List list;
    
    private InputMultiplexer inputMultiplexer;

    public SelectGameScreen(TrappedTilDoomGame trappedTilDoomGame) {
        super(trappedTilDoomGame);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new SelectGameInputProcessor(this));

        list = new List(new String[0], skin);
        ScrollPane listScroll = new ScrollPane(list, skin);
        listScroll.setScrollingDisabled(true, false);

        TextButton loadGameButton = new TextButton("Load Game", skin);
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.gameSlot = new GameSlot(list.getSelection());
                if (game.gameSlot.validate()) {
                    game.setScreen(game.playGameScreen);
                    return;
                }
                

                Dialog dialog = new Dialog("Error", skin, "dialog") {
                    @Override
                    protected void result (Object object) {
                        if ((boolean)object) {
                            game.gameSlot.delete();
                            game.gameSlot = null;
                            refreshList();
                        }
                    }
                };

                dialog.text("The file you selected is corrupt.\nDelete file from disk?")
                      .button("Yes", true)
                      .button("No", false)
                      .key(Keys.ENTER, true)
                      .key(Keys.ESCAPE, false)
                      .show(stage);
            }
        });
         TextButton deleteGameButton = new TextButton("Delete Game", skin);
        deleteGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.gameSlot=new GameSlot(list.getSelection());
                


                Dialog dialog = new Dialog("Delete", skin, "dialog") {
                    @Override
                    protected void result (Object object) {
                        if ((boolean)object) {
                            game.gameSlot.delete();
                            game.gameSlot = null;
                            refreshList();
                        }
                    }
                };

                dialog.text("You are about to delete the game save: "+list.getSelection()+" this save will be lost forever, do you want to do this?")
                      .button("Yes", true)
                      .button("No", false)
                      .key(Keys.ENTER, true)
                      .key(Keys.ESCAPE, false)
                      .show(stage);
            }
        });

      

        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                returnToMainMenu();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(listScroll).size(500, 500);
        table.row();
        table.add(loadGameButton);
        table.row();
        table.add(deleteGameButton);
        table.row();
        table.add(backButton);

        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(width, height, true);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        refreshList();
    }

    @Override
    public void update(float delta) {
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

    public void returnToMainMenu() {
        game.setScreen(game.mainMenuScreen);
    }

    private void refreshList() {
        FileHandle savedGamesDirectory = Gdx.files.local("saves");
        FileHandle[] savedGames = savedGamesDirectory.list("sav");

        String[] fileNames = new String[savedGames.length];
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = savedGames[i].nameWithoutExtension();
        }

        list.setItems(fileNames);
    }
}
