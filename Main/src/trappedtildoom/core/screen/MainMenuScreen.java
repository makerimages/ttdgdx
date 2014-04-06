package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.input.MainMenuInputProcessor;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.util.SleepUtil;

public class MainMenuScreen extends TrappedTilDoomScreen {

    private Stage stage = new Stage();
    private Skin skin = new Skin(Gdx.files.internal("res/ui/uiskin.json"));
    
    private InputMultiplexer inputMultiplexer;
    
    public MainMenuScreen(TrappedTilDoomGame trappedTilDoomGame) {
        super(trappedTilDoomGame);
        
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(commonInputProcessor);
        inputMultiplexer.addProcessor(new MainMenuInputProcessor(game));

        TextButton newGameButton = new TextButton("Start New Game", skin);
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.newGameScreen);
            }
        });

        TextButton loadGameButton = new TextButton("Load Game", skin);
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                game.setScreen(game.selectGameScreen);
            }
        });

        TextButton exitGameButton = new TextButton("Exit Game", skin);
        exitGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getSoundManager().play(SoundManager.TrappedTilDoomSound.CLICK);
                SleepUtil.Sleep(500);
                Gdx.app.exit();
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

        Table table = new Table();
        table.setFillParent(true);

        table.add(newGameButton);
        table.row();
        table.add(loadGameButton);
        table.row();
        table.add(optionsButton);
        table.row();
        table.add(exitGameButton);
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

        if (game.gameSlot != null) {
            game.gameSlot = null;
        }
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
