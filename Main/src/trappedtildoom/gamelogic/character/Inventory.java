package trappedtildoom.gamelogic.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import trappedtildoom.gamelogic.item.Item;
import trappedtildoom.ui.GuiSlot;

/**
 * Created with IntelliJ IDEA.
 * User: Kodukas                          Reffers to Makerimages
 * Date: 20.11.13
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */
public class Inventory
{
    public static int selected;
    GuiSlot slots[]=new GuiSlot[9];

    public Inventory()
    {
        for(int i=0;i<9;i++)
        {
            slots[i]=new GuiSlot(i, Gdx.graphics.getWidth() / 2 - 170+i+39*i,0);
        }
        setSelection(3);
    }
    public void setSelection(int i)
    {
        if (!slots[i-1].selected)
        {
            slots[i-1].toggleSelection(true);
            slots[selected].toggleSelection(false);
            selected=i-1;
        }
    }
    public void draw(SpriteBatch spriteBatch)
    {
        for(int i=0;i<9;i++)
        {
            if(slots[i]!=null)
            {
                slots[i].draw(spriteBatch, slots[i].x, slots[i].y);
            }
        }
    }
    public Item getCurrentItem()
    {
        return slots[selected].contents[0];
    }
    public void removeSelectedItem()
    {
       slots[selected].contents[0]=null;
    }

    public void addItemToInventory(int id, Item item)
    {
       slots[id].contents[0]=item;
    }
}
