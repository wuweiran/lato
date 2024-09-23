package ardash.lato.weather;

import com.badlogic.gdx.graphics.Color;

public interface SkyColorChangeListener {

    /**
     * Change a color in the defined period of time.
     *
     * @param targetTop    The target color for the top of the sky.
     * @param targetBottom The target color for the bottom of the sky.
     * @param seconds      The time period to perform the change in seconds.
     */
    void onSkyColorChangeTriggered(Color targetTop, Color targetBottom, float seconds);

}
