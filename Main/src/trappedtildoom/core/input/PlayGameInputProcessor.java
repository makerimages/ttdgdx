package trappedtildoom.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.screen.PlayGameScreen;
import trappedtildoom.core.service.SoundManager;

public class PlayGameInputProcessor extends InputAdapter {
    private final PlayGameScreen screen;
    private final TrappedTilDoomGame game;
    
    public PlayGameInputProcessor(TrappedTilDoomGame game,PlayGameScreen screen) {
        this.screen = screen;
        this.game=game;
    }

    @Override
    public boolean keyDown(int key) {
        switch (key) {
            case Keys.ESCAPE:
                this.game.setScreen(game.pauseScreen);
                return true;

            case Keys.G:
                screen.generateNewTerrain();
                return true;
           
            case Keys.MINUS:
                screen.getPlayer().health -= 5;
                screen.getSoundManager().play(SoundManager.TrappedTilDoomSound.HURT);
                return true;

            case Keys.PLUS:
                screen.getPlayer().health += 5;
                return true;

            case Keys.N:
                Gdx.app.exit();
                return true;

            case Keys.T:
                screen.resetLightning();
                return true;

            case Keys.NUM_1:
            case Keys.NUM_2:
            case Keys.NUM_3:
            case Keys.NUM_4:
            case Keys.NUM_5:
            case Keys.NUM_6:
            case Keys.NUM_7:
            case Keys.NUM_8:
            case Keys.NUM_9:
                screen.getPlayer().inventory.setSelection(key - Keys.NUM_1 + 1);
                return true;

        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.RIGHT) {
            screen.onAction();
            return true;
        }

        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        //screen.hotBar.changeSelection(amount);
        return true;
    }
}
