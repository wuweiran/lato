package ardash.lato.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.actions.Actions3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;

public class Scarf extends Group3D {

    public static final float SEG_LEN = 0.3f;
    public static final float SEG_THICK = 0.1f;
    public static final Color SCARF_COLOUR_1 = new Color(0xbc6d56ff);
    public static final Color SCARF_COLOUR_2 = new Color(0xa83c40ff);
    int currentAmountOfSegments = -1;
    private float[] x = new float[20];
    private float[] y = new float[20];

    public Scarf() {
        TextureRegion sTexture = A.getTextureRegion(ARAsset.FOG_PIX);

        ModelBuilder mb = new ModelBuilder();
        setName("scarf");
        for (int i = 0; i < 20; i++) {
            Image3D img = new Image3D(SEG_LEN, SEG_THICK, sTexture, mb);
            addActor(img);
            if (i % 2 == 0)
                img.setColor(SCARF_COLOUR_1);
            else
                img.setColor(SCARF_COLOUR_2);
            img.addAction(Actions3D.fadeOut(0f));
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        dragSegment(0, getX(), getY());
        for (int i = 0; i < x.length - 1; i++) {
            dragSegment(i + 1, x[i], y[i]);
        }
    }

    private void dragSegment(int i, float xin, float yin) {
        float dx = xin - x[i];
        float dy = yin - y[i];
        float angle = MathUtils.atan2(dy, dx);
        x[i] = xin - MathUtils.cos(angle) * SEG_LEN;
        y[i] = yin - MathUtils.sin(angle) * SEG_LEN;
        moveSegment(i, x[i], y[i], angle);
    }

    private void moveSegment(int i, float x, float y, float a) {
        a *= MathUtils.radiansToDegrees;
        final Actor3D ch = getChild(i);
        ch.setPosition(x, y);
        ch.setRotation(a);
    }

    @Override
    public void setPosition(float x, float y) {
        getChild(0).setPosition(x, y);
    }

    @Override
    public float getX() {
        return getChild(0).getX();
    }

    @Override
    public float getY() {
        return getChild(0).getY();
    }

    /**
     * sets the length of the scarf from 0 to 1 (0% to 100%)
     * The amount of visible sections (0-20) will be adjusted accordingly
     *
     * @param percentage
     */
    public void setLength(float percentage) {
        final float lerped = MathUtils.lerp(0, 20, percentage);
        final float clamped = MathUtils.clamp(lerped, 0, 20);
        final int rounded = MathUtils.roundPositive(clamped);
        setLength(rounded);
    }

    private void setLength(int amountOfSegments) {
        if (amountOfSegments == currentAmountOfSegments)
            return;
        for (int i = 0; i < x.length; i++) {
            final Actor3D ch = getChild(i);
            ch.clearActions();
            if (i < amountOfSegments)
                ch.addAction(Actions3D.fadeIn(1f));
            else
                ch.addAction(Actions3D.fadeOut(1f));
//			ch.setVisible(i<amountOfSegments);
        }

        currentAmountOfSegments = amountOfSegments;
    }
}
