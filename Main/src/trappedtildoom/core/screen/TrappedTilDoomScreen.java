package trappedtildoom.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import trappedtildoom.core.TrappedTilDoomGame;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.util.ScreenShotUtil;

public abstract class TrappedTilDoomScreen implements Screen {
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final BitmapFont font = new FreeTypeFontGenerator(Gdx.files.internal("res/fonts/bavarg.ttf")).generateFont(14);
    private final String versionString = TrappedTilDoomGame.version;
    
    protected final TrappedTilDoomGame game;
    protected final InputAdapter commonInputProcessor = new CommonInputProcessor();
    
    protected TrappedTilDoomScreen(TrappedTilDoomGame game) {
        this.game = game;
    }

    protected void update(float delta) {

    }

    protected void draw(float delta) {

    }

    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);

        spriteBatch.begin();
        font.draw(spriteBatch, versionString, 10, 20);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        font.dispose();
        spriteBatch.dispose();
    }

    public SoundManager getSoundManager() {
        return game.getSoundManager();
    }

    private class CommonInputProcessor extends InputAdapter {
        @Override
        public boolean keyDown(int key) {
            switch (key) {
                case Keys.F8:
                    ScreenShotUtil.makeCapture();
                    return true;

                case Keys.B:
                    game.setScreen(game.bodyPodBeginningScreen);
                    return true;
            }
            return false;
        }
    }
}
