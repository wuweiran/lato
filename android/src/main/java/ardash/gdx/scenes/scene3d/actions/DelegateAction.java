package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.utils.Pool;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * Base class for an action that wraps another action.
 *
 * @author Nathan Sweet
 */
abstract public class DelegateAction extends Action3D {
    protected Action3D action;

    public Action3D getAction() {
        return action;
    }

    /**
     * Sets the wrapped action.
     */
    public void setAction(Action3D action) {
        this.action = action;
    }

    abstract protected boolean delegate(float delta);

    @Override
    public final boolean act(float delta) {
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool inside the delegate action.
        try {
            return delegate(delta);
        } finally {
            setPool(pool);
        }
    }

    @Override
    public void restart() {
        if (action != null) action.restart();
    }

    @Override
    public void reset() {
        super.reset();
        action = null;
    }

    @Override
    public void setActor(Actor3D actor) {
        if (action != null) action.setActor(actor);
        super.setActor(actor);
    }

    @Override
    public String toString() {
        return super.toString() + (action == null ? "" : "(" + action + ")");
    }
}
