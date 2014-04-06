package trappedtildoom.gamelogic.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.gamelogic.effects.LightningEffect;
import trappedtildoom.core.screen.PlayGameScreen;
import trappedtildoom.core.service.SoundManager;
import trappedtildoom.core.util.ClassUtil;

import java.util.Random;

public class GameWorldRenderer implements Disposable {

    private final GameWorld gameWorld;
    private final Random random = new Random();
    private final PlayGameScreen screen;

    private LightningEffect lightningEffect;

    public GameWorldRenderer(GameWorld gameWorld, PlayGameScreen screen) {
        this.gameWorld = gameWorld;
        this.screen = screen;
    }

    public void drawBackground() {
        float brightness = gameWorld.getBrightness();
        Color clearColor = new Color(0.0f * brightness, 0.7f * brightness, 1.0f * brightness, 1.0f);

        if (lightningEffect != null) {
            clearColor = lightningEffect.getBackgroundColor(clearColor);
        }

        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public void draw(OrthographicCamera camera) {
        if (lightningEffect != null) {
            lightningEffect.draw(camera);
        }
    }

    public void update(float delta) {
        gameWorld.update(delta);

        if (lightningEffect != null) {
            lightningEffect.update(delta);
            if (lightningEffect.isDone()) {
                lightningEffect.dispose();
                lightningEffect = null;
                gameWorld.thunder = false;
            }
        } else if (gameWorld.time > 22.00f && random.nextInt(1000) > 995) {
            resetLightning();
        }
    }

    @Override
    public void dispose() {
        ClassUtil.dispose(lightningEffect);
    }

    public void resetLightning() {
        if (lightningEffect != null) {
            return;
        }

        gameWorld.thunder = true;

        float minY = Gdx.graphics.getHeight();
        float maxY = 0.7f * Gdx.graphics.getHeight();

        Random random = new Random();

        screen.getSoundManager().play(SoundManager.TrappedTilDoomSound.THUNDER);

        lightningEffect = new LightningEffect(
                new Vector2(random.nextInt(Gdx.graphics.getWidth()), minY),
                new Vector2(random.nextInt(Gdx.graphics.getWidth()), maxY)
        );
    }
}
