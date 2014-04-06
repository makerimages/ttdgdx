package trappedtildoom.core.util;

import com.badlogic.gdx.utils.Disposable;

public class ClassUtil {
    
    public static void dispose(Disposable disposable) {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
