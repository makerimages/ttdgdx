package trappedtildoom.core.thread;

import trappedtildoom.core.screen.PlayGameScreen;
import trappedtildoom.core.service.AbsoluteLogger;
import trappedtildoom.core.service.SoundManager;

public class FoodDecreaserThread implements Runnable {
    private static final long SECONDS = 1000;
    private static final long MINUTES = 60 * SECONDS;
    
    private final PlayGameScreen screen;

    public FoodDecreaserThread(PlayGameScreen screen) {
        this.screen = screen;
    }

    @Override
    public void run() {
        try {
            while (true) {
                long timeInterval = 1 * MINUTES;
                
                screen.getPlayer().food -= 5;
                screen.getSoundManager().play(SoundManager.TrappedTilDoomSound.FOOD_LOWER);
                
                if (screen.getPlayer().food <= 20) {
                    screen.getPlayer().health -= 10;
                    screen.getSoundManager().play(SoundManager.TrappedTilDoomSound.HURT);
                }
                
                if (screen.getPlayer().food < 10) {
                    screen.getPlayer().health -= 20;
                    screen.getSoundManager().play(SoundManager.TrappedTilDoomSound.HURT);
                    timeInterval = 5 * SECONDS;
                }
                
                Thread.sleep(timeInterval);
            }
        } catch (InterruptedException ex) {
            AbsoluteLogger.info("Food decreaser thread stopped.");
        } catch (Exception ex) {
            AbsoluteLogger.severe(null, ex);
        }
    }
}
