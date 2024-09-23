package ardash.lato.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;

public class MoreActions extends Actions {

    /**
     * Transitions from the color at the time this action starts to the specified color.
     */
    static public NoAlphaColorAction2D noAlphaColor(Color color, float duration) {
        return noAlphaColor(color, duration, null);
    }

    /**
     * Transitions from the color at the time this action starts to the specified color.
     */
    static public NoAlphaColorAction2D noAlphaColor(Color color, float duration, Interpolation interpolation) {
        NoAlphaColorAction2D action = action(NoAlphaColorAction2D.class);
        action.setNoAlphaEndColor(color);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public FloatAction floata(float start, float end, float duration) {
        FloatAction action = action(FloatAction.class);
        action.setStart(start);
        action.setEnd(end);
        action.setDuration(duration);
        return action;
    }

}
