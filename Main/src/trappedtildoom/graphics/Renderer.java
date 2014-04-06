package trappedtildoom.graphics;

import trappedtildoom.gamelogic.gameobject.GameObject;

public interface Renderer<T extends GameObject> {
    void render(T gameObject);
}
