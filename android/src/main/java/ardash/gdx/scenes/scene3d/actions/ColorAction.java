package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ColorAction extends TemporalAction {
    private final Color end = new Color();
    private float startR, startG, startB, startA;
    private Color color;

    protected void begin() {
        if (color == null) color = getActor().getColor();
        startR = color.r;
        startG = color.g;
        startB = color.b;
        startA = color.a;
    }

    protected void update(float percent) {
        if (percent == 0)
            color.set(startR, startG, startB, startA);
        else if (percent == 1)
            color.set(end);
        else {
            float r = startR + (end.r - startR) * percent;
            float g = startG + (end.g - startG) * percent;
            float b = startB + (end.b - startB) * percent;
            float a = startA + (end.a - startA) * percent;
            color.set(r, g, b, a);
        }
    }

    public void reset() {
        super.reset();
        color = null;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Sets the color to modify. If null (the default), the {@link #getActor() actor's} {@link Actor#getColor() color} will be
     * used.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getEndColor() {
        return end;
    }

    /**
     * Sets the color to transition to. Required.
     */
    public void setEndColor(Color color) {
        end.set(color);
    }
}
