package ardash.lato.weather;

import com.badlogic.gdx.graphics.Color;

public interface AmbientColorChangeListener {

    /**
     * Change a color in the defined period of time.
     *
     * @param target  The target color.
     * @param seconds The time period to perform the change in seconds.
     */
    void onAmbientColorChangeTriggered(Color target, float seconds);

}
