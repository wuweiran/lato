package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.graphics.Color;

public class NoAlphaColorAction extends ColorAction {
    private final Color noAlphaEnd = new Color();
    private float startR, startG, startB;

    protected void begin() {
        if (getColor() == null) setColor(actor.getColor());
        startR = getColor().r;
        startG = getColor().g;
        startB = getColor().b;
    }

    @Override
    protected void update(float percent) {
        if (percent == 0) {
            getColor().r = startR;
            getColor().g = startG;
            getColor().b = startB;
        } else if (percent == 1) {
            getColor().r = noAlphaEnd.r;
            getColor().g = noAlphaEnd.g;
            getColor().b = noAlphaEnd.b;
        } else {
            float r = startR + (noAlphaEnd.r - startR) * percent;
            float g = startG + (noAlphaEnd.g - startG) * percent;
            float b = startB + (noAlphaEnd.b - startB) * percent;
            getColor().r = r;
            getColor().g = g;
            getColor().b = b;
        }
    }

    /**
     * Sets the color to transition to. Required.
     */
    public void setNoAlphaEndColor(Color color) {
        noAlphaEnd.set(color);
    }

}
