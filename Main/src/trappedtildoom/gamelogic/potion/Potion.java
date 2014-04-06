/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trappedtildoom.gamelogic.potion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import trappedtildoom.gamelogic.gameobject.Player;

/**
 *
 * @author Makerimages
 */
public class Potion
{
    public  ParticleEffect effect;
    public int effectDuration;
    public static Potion potionSpeed;
    public PotionType potionType;
    Player player;
    public Potion(ParticleEffect effect, int effectDuration,FileHandle fh,PotionType potionType)
    {
        this.effect=effect;
        this.effectDuration=effectDuration;
        this.potionType=potionType;
        this.effect.load(fh, Gdx.files.internal("res/particles/"));
    }

      public void usePotion(Player player,SpriteBatch sb, float delta)
      {
              this.player=player;
            this.effect.start();
          this.effect.setPosition(this.player.x+16,this.player.y+16);
          this.effect.draw(sb,delta);
          switch (this.potionType)
          {
              case SpeedPotion:
                        while(this.effectDuration>0)
                        {
                            player.increaseExperience(4);
                            this.effectDuration--;
                        }
                  case EmptyPotion:
                      this.effect.dispose();
          }
          if(this.effectDuration<=0)
          {
              this.player.applyPotion(new Potion(this.effect,4,Gdx.files.internal("res/particles/test.p"), PotionType.EmptyPotion));

          }
      }



}
