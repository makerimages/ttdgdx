package trappedtildoom.gamelogic.item;

import com.badlogic.gdx.graphics.Texture;
import java.io.Serializable;
import java.util.Random;

public abstract class Item implements Serializable {

    protected static transient final Random random = new Random();
    public Texture texture;
    public final String name;
    public final int tier;
    public  int durability;
    protected Item(String name, int tier,int dur) {
        this.name = name;
        this.tier = tier;
        this.durability=dur;
    }

    public abstract boolean use(Object target);
}
