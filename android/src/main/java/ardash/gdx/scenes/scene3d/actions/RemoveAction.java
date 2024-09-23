package ardash.gdx.scenes.scene3d.actions;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * Removes an action from an actor.
 *
 * @author Nathan Sweet
 */
public class RemoveAction extends Action3D {
    private Actor3D target;
    private Action3D action;

    @Override
    public boolean act(float delta) {
        (target != null ? target : actor).removeAction(action);
        return true;
    }

    public Actor3D getTarget() {
        return target;
    }

    public void setTarget(Actor3D actor) {
        this.target = actor;
    }

    public Action3D getAction() {
        return action;
    }

    public void setAction(Action3D action) {
        this.action = action;
    }

    @Override
    public void reset() {
        super.reset();
        target = null;
        action = null;
    }
}
