package ardash.lato.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;

import ardash.gdx.scenes.scene3d.actions.Actions3D;
import ardash.gdx.scenes.scene3d.actions.FloatAction;

public class Actions extends Actions3D {
    static public GravityAction gravity() {
        return action3d(GravityAction.class);
    }

    /**
     * Transitions from the color at the time this action starts to the specified color.
     */
    static public NoAlphaColorAction noAlphaColor(Color color, float duration) {
        return noAlphaColor(color, duration, null);
    }

    /**
     * Transitions from the color at the time this action starts to the specified color.
     */
    static public NoAlphaColorAction noAlphaColor(Color color, float duration, Interpolation interpolation) {
        NoAlphaColorAction action = action3d(NoAlphaColorAction.class);
        action.setNoAlphaEndColor(color);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    static public FloatAction floata(float start, float end, float duration) {
        FloatAction action = action3d(FloatAction.class);
        action.setStart(start);
        action.setEnd(end);
        action.setDuration(duration);
        return action;
    }

}
