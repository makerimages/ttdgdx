package trappedtildoom.core.screen;

import com.badlogic.gdx.assets.AssetManager;
import trappedtildoom.core.TrappedTilDoomGame;

public class LoadingScreen extends TrappedTilDoomScreen {

    private AssetManager manager = new AssetManager();

    public LoadingScreen(TrappedTilDoomGame game) {
        super(game);
    }

    @Override
    protected void update(float delta) {
        if (manager.update()) {
            game.setScreen(game.mainMenuScreen);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
    }
}
