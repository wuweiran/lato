package ardash.gdx.scenes.scene3d.actions;

/**
 * Sets the actor's rotation from its current value to a relative value.
 *
 * @author Nathan Sweet
 */
public class RotateByAction extends RelativeTemporalAction {
    private float amountX, amountY, amountZ;

    @Override
    protected void updateRelative(float percentDelta) {
        actor.rotate(amountX * percentDelta, amountY * percentDelta, amountZ * percentDelta);
    }

    public void setAmount(float x, float y, float z) {
        amountX = x;
        amountY = y;
        amountZ = z;
    }

    public float getAmountX() {
        return amountX;
    }

    public void setAmountX(float x) {
        amountX = x;
    }

    public float getAmountY() {
        return amountY;
    }

    public void setAmountY(float y) {
        amountY = y;
    }

    public float getAmountZ() {
        return amountZ;
    }

    public void setAmountZ(float z) {
        amountZ = z;
    }
}
