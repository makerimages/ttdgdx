package trappedtildoom.gamelogic.environmentobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class EnvironmentObject implements Disposable
{
    public String name;
    public boolean isMovable;
    public boolean isBreakable;
    public static float OBJECT_SIZE = 32f;

    protected Texture getTexture() 
    {
        return null;
    }

    public EnvironmentObject(String name) 
    {
        this.name = name;
    }

    public void setMovable(boolean isMovable) 
    {
        this.isMovable = isMovable;
    }

    public void setBreakable(boolean isBreakable) 
    {
        this.isBreakable = isBreakable;
    }

    public void draw(SpriteBatch batch, float x, float y) 
    {
        Texture texture = getTexture();
        if (texture == null)
            return;

        batch.draw(texture, x, y, OBJECT_SIZE, OBJECT_SIZE);
    }

    @Override
        public void dispose() 
    {
        Texture texture = getTexture();
        if (texture != null) 
        {
            texture.dispose();
        }
    }
    public  void Update()
    {
       
    }
}

