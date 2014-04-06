package trappedtildoom.gamelogic.gameobject;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import trappedtildoom.core.service.AbsoluteLogger;
import trappedtildoom.gamelogic.character.Inventory;
import trappedtildoom.gamelogic.character.PlayerModel;
import trappedtildoom.gamelogic.item.Item;
import trappedtildoom.gamelogic.item.PorkChopItem;
import trappedtildoom.graphics.PlayerRenderer;

import java.io.Serializable;
import java.util.Random;

import trappedtildoom.gamelogic.potion.Potion;

public class Player extends GameObject implements Serializable {

    public static final int MAX_FOOD = 100;
    public static final int MAX_HEALTH = 100;
    public static final int MAX_XP = 190;
    public static Potion sp;
    Random rand=new Random();
    public static Inventory inventory;
    private transient PlayerModel playerModel;
    private transient PlayerRenderer renderer;

    public int selectedItem = 0;

    public float x;
    public float y;

    public int xp = 0;
    public int level = 0;
    public int health = MAX_HEALTH;
    public int food = MAX_FOOD;

    public int hairTextureId = 0;
    public int headTextureId = 0;
    public int bodyTextureId = 0;
    public int legTextureId = 0;
    public int shoeTextureId = 0;

    public boolean flipX = false;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        inventory=new Inventory();
        PorkChopItem pI=new PorkChopItem();
        inventory.addItemToInventory(2,pI);

    }

    public void setRenderer(PlayerRenderer renderer) {
        this.renderer = renderer;
    }

    public void initializePlayerModel(World world) {
        playerModel = new PlayerModel(world, x, y, this);
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public Item getCurrentItem() {
        return inventory.getCurrentItem();
    }

    public boolean useCurrentItem() {
        Item item = getCurrentItem();
        if (item == null) {
            return false;
        }
        if (item.use(this)) {
            System.out.println(item.durability);
            item.durability--;
            System.out.println(item.durability);

            if(item.durability<=0)
            {
                //Nowork
                inventory.removeSelectedItem();
            }
         }
        return true;
    }
    public void increaseExperience(int amount) {
        xp += amount;
        while (xp >= MAX_XP) {
            xp -= MAX_XP;
            level += 1;
        }
    }

    public void update() {
        x = playerModel.getPositionX();
        y = playerModel.getPositionY();

        if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
            flipX = false;
        }

        if (!Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D)) {
            flipX = true;
        }

    }

    public void consumeFood(int amount) {
        String message = "Eating food (" + food + " -> ";
        food = Math.min(MAX_FOOD, food + amount);
        AbsoluteLogger.info(message + food + ")");
    }

   /*
    public boolean addItemHotBar(Item item) {
        return inventory.addActiveItem(item);
    }

    public boolean addItemInventory(Item item) {
        return inventory.addItem(item);
    }
       */
    public void decreaseHealth(int amount) {
        health = Math.max(0, health - amount);
    }

    public void draw(SpriteBatch sb,float delta) {
        renderer.render(this);

        if(getCurrentItem()!=null)
        {
        sb.draw(getCurrentItem().texture,x,y);
        }
        sp.usePotion(this,sb,delta);


    }
    public void applyPotion(Potion potion)
    {
        sp=potion;
        sp.effect.start();

    }

    
}
