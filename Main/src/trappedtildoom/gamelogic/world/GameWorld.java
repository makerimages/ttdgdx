package trappedtildoom.gamelogic.world;

import java.io.Serializable;

public class GameWorld implements Serializable {

    /**
     * One second in real world (delta time) corresponds to sixty seconds in game world.
     * One day is exactly 24 minutes.
     */
    public static final float WORLD_TIME_FACTOR = 1.0f / 60.0f;

    /**
     * What time the day is brightest.
     */
    public static final float NOON_TIME = 14.0f;

    public boolean rain;
    public boolean snow;
    public boolean thunder;
    public boolean earthquake;
    public float time = 10.00f;
    public int numberOfDays = 0;

    public void toggleRain() {
        rain = !rain;
    }

    public void toggleSnow() {
        snow = !snow;
    }

    public void toggleThunder() {
        thunder = !thunder;
    }

    /**
     * Update game world environment in every frame instead of quite long time interval.
     * Gives smoother animations and more opportunities for various effects.
     * @param delta Time difference in seconds comparing to previous frame.
     */
    public void update(float delta) {
        // Every frame update game world time. Multiplication with WORLD_TIME_FACTOR converts real time to game time.
        // Modulus operator guarantees that game time is between [0; 24) and value that is greater or equal to 24
        // gets turned into 0.
        float oldTime = time;
        time = (time + delta * WORLD_TIME_FACTOR) % 24;
        if (time < oldTime) { // Check if new day has begun
            numberOfDays++;
        }
    }

    /**
     * How bright the world should be.
     * Currently calculates using time of day, so that at 14 the world is brightest and at 2 its completely dark.
     * Could also include other factors such as if it's raining or snowing at the moment.
     * @return Brightness value from 0 to 1 where 0 is completely dark and 1 completely bright.
     */
    public float getBrightness() {
        // Use cosine function to give longer dark and bright times and faster changes between them.
        return (float)(Math.cos(Math.PI * (time - NOON_TIME) / 12) + 1) / 2;
    }

    /**
     * Get current time in 24 hour format (could be customizable, to use AM/PM format?).
     * @return World time transformed into clock display with hours and minutes.
     */
    public String getTime() {
        // Integer part of time gives hours
        int hours = (int)Math.floor(time);
        // Decimal part needs to be in base 60 to get minutes
        int minutes = (int)Math.floor((time % 1.0f) * 60f);
        return String.format("%d:%02d", hours, minutes);
    }
}
