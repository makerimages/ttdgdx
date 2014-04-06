package trappedtildoom.gamelogic.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class PorkChopItem extends FoodItem {

    public PorkChopItem() {
        super("Pork Chop", 1,1);
        this.texture=new Texture(Gdx.files.internal("res/textures/items/porkChops.png"));
    }
}
