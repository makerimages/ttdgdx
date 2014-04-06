package trappedtildoom.core.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MathUtils {
    public static float distancePointRect(Vector2 point, Rectangle rectangle) {
        float distanceTL = new Vector2(rectangle.x, rectangle.y).sub(point).len();
        float distanceTR = new Vector2(rectangle.x + rectangle.width, rectangle.y).sub(point).len();
        float distanceBL = new Vector2(rectangle.x, rectangle.y + rectangle.height).sub(point).len();
        float distanceBR = new Vector2(rectangle.x + rectangle.width, rectangle.y + rectangle.height).sub(point).len();
        return Math.abs(Math.min(Math.min(distanceTL, distanceTR), Math.min(distanceBL, distanceBR)));
    }
}
