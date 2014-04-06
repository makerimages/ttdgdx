/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trappedtildoom.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Disposable;
import trappedtildoom.gamelogic.character.Inventory;
import trappedtildoom.gamelogic.item.PorkChopItem;
import trappedtildoom.gamelogic.item.WoodenAxeItem;

/**
 *
 * @author Makerimages
 */
public class InventoryWindow implements Disposable
{
    PorkChopItem iP=new PorkChopItem();
    WoodenAxeItem wI=new WoodenAxeItem();
    public static int selected=1;
    Inventory containedInventory ;
    public InventoryWindow(Skin skin,Inventory inventory)
    {
        containedInventory=inventory;


    }
    
    public void draw(SpriteBatch spriteBatch)
    {
                        containedInventory.draw(spriteBatch);
    }
    @Override
    public void dispose() {

    }

}
