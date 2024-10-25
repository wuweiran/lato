package ardash.lato.actions;

import ardash.gdx.scenes.scene3d.Action3D;

/**
 * Similar to MoveByAction, but runs infinitely and has an acceleration.
 * This does not add a gravity to Actors. It only adds the relative movement that looks like gravity.
 * So this cannot be constantly added to and Actor but rather has the be re-added for each jump.
 */
public class GravityAction extends Action3D {
    static final float gravity = 9.80665f; // m/s/s
    float vspeed = 0;

    @Override
    public void reset() {
        super.reset();
        vspeed = 0;
    }

    @Override
    public boolean act(float delta) {
        vspeed += (gravity * delta);
        actor.moveBy(0, -vspeed * delta);
        return false; // infinite (until removed from actor)
    }

}
