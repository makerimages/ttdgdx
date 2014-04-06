package trappedtildoom.gamelogic.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;

public class LightningEffect implements Disposable
{

    private final float[] lightningBolt;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private boolean done = false;
    private Color lineColor = new Color(Color.WHITE);
    private float lineWidth = 1.0f;
    private int first = 0;
    private int size = 0;
    private float time = 0.0f;

    public LightningEffect(Vector2 vecFrom, Vector2 vecTo) 
    {
        LightningGenerator generator = new LightningGenerator(vecFrom, vecTo);
        lightningBolt = generator.getLightningBolt();
    }

    public boolean isDone() 
    {
        return done;
    }

    public Color getBackgroundColor(Color defaultColor) 
    {
        if (time < 0.1f || time > 0.15f && time < 0.2f) 
        {
            return Color.WHITE;
        }
        return defaultColor;
    }

    public void update(float delta)
    {
        time += delta;

        int totalSize = lightningBolt.length / 2;
        if (time < 0.4f) {
            size = (int)Math.min(totalSize, (time / 0.3f) * totalSize);
            lineColor.a = Math.min(1.0f, (time / 0.3f));
            lineWidth = Math.min(3.0f, (time / 0.3f) * 3.0f);
        } else if (time < 0.8f) {
            float normTime = time - 0.4f;
            first = (int)Math.min(totalSize, (normTime / 0.3f) * totalSize);
            lineColor.a = 1.0f - Math.min(1.0f, (normTime / 0.3f));
            lineWidth = 3.0f - Math.min(3.0f, (normTime / 0.3f) * 3.0f);
        } else {
            done = true;
        }
    }

    public void draw(OrthographicCamera camera) 
    {
        Gdx.gl.glLineWidth(lineWidth);

        Gdx.gl20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl20.glEnable(GL10.GL_BLEND);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float amount = 1.0f / size;
        for (int i = first + 1; i < size; i++) {
            Color color = new Color(lineColor);
            color.a -= amount;
            shapeRenderer.setColor(color);
            shapeRenderer.line(
                    lightningBolt[2 * i - 2],
                    lightningBolt[2 * i - 1],
                    lightningBolt[2 * i],
                    lightningBolt[2 * i + 1]);
        }
        shapeRenderer.end();

        Gdx.gl.glLineWidth(1);
        Gdx.gl20.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void dispose()
    {
        shapeRenderer.dispose();
    }

    private class LightningGenerator 
    {
        private static final float DETAIL_LEVEL = 2;

        private final ArrayList<Vector2> vertices = new ArrayList<>();

        public LightningGenerator(Vector2 vecFrom, Vector2 vecTo) {
            float displacement = 0.2f * vecTo.tmp().sub(vecFrom).len();
            calculateLightning(vecFrom, vecTo, displacement);
        }

        public float[] getLightningBolt()
        {
            float[] lightningBolt = new float[2 * vertices.size()];
            for (int i = 0; i < vertices.size(); i++) {
                lightningBolt[2 * i] = vertices.get(i).x;
                lightningBolt[2 * i + 1] = vertices.get(i).y;
            }
            return lightningBolt;
        }

        private void calculateLightning(Vector2 vecFrom, Vector2 vecTo, float displacement)
        {
            if (displacement < DETAIL_LEVEL) {
                vertices.add(vecFrom);
                vertices.add(vecTo);
                return;
            }

            Vector2 middle = new Vector2((vecFrom.x + vecTo.x) / 2, (vecFrom.y + vecTo.y) / 2);
            middle.x += (Math.random() - 0.5) * displacement;
            middle.y += (Math.random() - 0.5) * displacement;

            calculateLightning(vecFrom, middle, displacement / 2);
            calculateLightning(middle, vecTo, displacement / 2);
        }
    }
}
