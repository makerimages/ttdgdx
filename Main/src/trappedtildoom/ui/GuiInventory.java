package trappedtildoom.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;


public class  GuiInventory extends Actor implements Disposable {

    public static final int ITEM_COUNT_X = 9;
    public static final int ITEM_COUNT_Y = 3;

          int y=0;

    private final BitmapFont font;
    private final Texture texture = new Texture(Gdx.files.internal("res/textures/guis/Hotbar.png"));

    public int selectedItem = 0;

    //public IItem[][] items = new IItem[ITEM_COUNT_X][ITEM_COUNT_Y];

    public GuiInventory(BitmapFont font) {
        super();
        setSize(364, 126);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.font = font;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    @Override
    public void draw(SpriteBatch spriteBatch, float parentAlpha) {
        Color color = getColor();
        spriteBatch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        spriteBatch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), 2, 42, getScaleX(), getScaleY(),
                getRotation(), 0, 0, 2, 42, false, false);

        for (int valueIndex = 0; valueIndex < ITEM_COUNT_X; valueIndex++) {
            float x = getX() + 2 + 40 * valueIndex;
              
                      //System.out.println(items[valueIndex][y]);
            spriteBatch.draw(texture, x, getY(), getOriginX(), getOriginY(), 40, 42, getScaleX(), getScaleY(),
                    getRotation(), 2, 0, 40, 42, false, false);

            if (selectedItem == valueIndex) {
                spriteBatch.draw(texture, x, getY(), getOriginX(), getOriginY(), 40, 42, getScaleX(), getScaleY(),
                        getRotation(), 46, 0, 40, 42, false, false);
            }
            
           // if (items[valueIndex][y]!= null) {
             //   spriteBatch.draw(items[valueIndex][valueIndex].getTexture(), x + 6, getY() - 6, getOriginX(), getOriginY(), 64, 64,
               //         getScaleX(), getScaleY(), getRotation(), 0, 0, 16, 16, false, false);
                //font.draw(spriteBatch, "" + items[valueIndex][valueIndex].getAmount(), x + 4, getY() + font.getLineHeight());
            y++;
            //}
        }

        spriteBatch.draw(texture, getX() + 2 + ITEM_COUNT_X * 40, getY(), getOriginX(), getOriginY(), 2, 42,
                getScaleX(), getScaleY(), getRotation(), 42, 0, 2, 42, false, false);
    }

    /*public void setItem(int positionX,int positionY, IItem item) {
        if (positionX >= 0 && positionX < ITEM_COUNT_X) {
            items[positionX][positionY] = item;
        }
    }*/

    
}

   
    

    

