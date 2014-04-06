package trappedtildoom.core.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import trappedtildoom.core.TrappedTilDoomGame;

public class MainMenuInputProcessor extends InputAdapter {
    
    private final TrappedTilDoomGame game;
    
    public MainMenuInputProcessor(TrappedTilDoomGame game) {
        this.game = game;
    }
        
    @Override
    public boolean keyDown(int key) {
        switch (key) {
            case Keys.ESCAPE:
                Gdx.app.exit();
                return true;
            case Keys.F8:
        
        }

        return false;
    }
}
