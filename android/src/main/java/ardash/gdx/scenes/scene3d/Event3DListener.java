package ardash.gdx.scenes.scene3d;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Low level interface for receiving events. Typically there is a listener class for each specific event class.
 *
 * @see InputListener
 * @see InputEvent
 */
public interface Event3DListener {
    /**
     * Try to handle the given event, if it is applicable.
     *
     * @return true if the event should be considered handled by scene2d.
     */
    boolean handle(Event3D event);
}
