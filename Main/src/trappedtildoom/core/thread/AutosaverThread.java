package trappedtildoom.core.thread;

import trappedtildoom.core.serialization.GameSlot;
import trappedtildoom.core.screen.PlayGameScreen;
import trappedtildoom.core.service.AbsoluteLogger;

public class AutosaverThread implements Runnable {
    private static final long MINUTES = 60 * 1000;
    
    private final PlayGameScreen screen;
    private final GameSlot gameSlot;

    public AutosaverThread(PlayGameScreen screen, GameSlot gameSlot) {
        this.gameSlot = gameSlot;
        this.screen = screen;
    }

    @Override
    public void run() {
        try {
            while (true) {
                screen.displayAutosaveMessage();
                gameSlot.save(screen.getData());
                Thread.sleep(5 * MINUTES);
            }
        } catch (InterruptedException ex) {
            AbsoluteLogger.info("Autosaver thread stopped.");
        } catch (Exception ex) {
            AbsoluteLogger.severe(null, ex);
        }
    }
}
