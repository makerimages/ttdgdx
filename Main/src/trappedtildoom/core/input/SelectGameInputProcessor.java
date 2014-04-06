package trappedtildoom.core.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import trappedtildoom.core.screen.SelectGameScreen;

public class SelectGameInputProcessor extends InputAdapter {
    private final SelectGameScreen screen;

    public SelectGameInputProcessor(SelectGameScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int key) {
        if (key == Keys.ESCAPE) {
            screen.returnToMainMenu();
            return true;
        }
        return false;
    }
}
