/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trappedtildoom.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import trappedtildoom.gamelogic.item.Item;
import trappedtildoom.gamelogic.item.PorkChopItem;


/**
 *
 * @author Makerimages
 */
public class GuiSlot 
{
    public boolean selected=false;
    int id;
    public int x;
    public int y;
    public Item[] contents=new Item[1];
    Texture backgroundTex=new Texture(Gdx.files.internal("res/textures/guis/Hotbar.png"));
    TextureRegion backRegion=new TextureRegion(backgroundTex,44,42);
    TextureRegion selectRegion=new TextureRegion(backgroundTex,44,0,44,42);

    Item allowsItem;
    public GuiSlot(int id, int x, int y)
    {
        this.id=id;
        this.x=x;
        this.y=y;

       

    }
    
    public void setAllowsItem(Item item)
    {
        this.allowsItem=item;
    }
    public boolean isItemValidForSlot(Item item)
    {
        if(item==allowsItem)
        {
            return true;
        }
        return false;
    }
    
    public void addItemToSlot(Item item)
    {
        if(allowsItem!=null)
        {
            if(isItemValidForSlot(item))
             {
                contents[0]=item;
             }
            else
             {
                contents[0]=null;
                System.err.println("Error adding item to slot- item not valid for slot");
             }
        }
        else
        {
            contents[0]=item;
        }
    }
     
    
    public void draw(SpriteBatch batch, float x, float y)
    {
      
           batch.draw(backRegion, x, y);
        if(selected)
        {
            batch.draw(selectRegion,x,y);
        }
       
        
          if(contents[0]!=null)
          {
              batch.draw(contents[0].texture, x+5, y-4, 64, 64);
          }
            
        
    }
    public void toggleSelection(boolean selection)
    {
        selected=selection;
    }
}
