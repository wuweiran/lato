package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * Executes a number of actions at the same time.
 *
 * @author Nathan Sweet
 */
public class ParallelAction extends Action3D {
    Array<Action3D> actions = new Array<>(4);
    private boolean complete;

    public ParallelAction() {
    }

    public ParallelAction(Action3D action1) {
        addAction(action1);
    }

    public ParallelAction(Action3D action1, Action3D action2) {
        addAction(action1);
        addAction(action2);
    }

    public ParallelAction(Action3D action1, Action3D action2, Action3D action3) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
    }

    public ParallelAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
        addAction(action4);
    }

    public ParallelAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
        addAction(action4);
        addAction(action5);
    }

    @Override
    public boolean act(float delta) {
        if (complete) return true;
        complete = true;
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            Array<Action3D> actions = this.actions;
            for (int i = 0, n = actions.size; i < n && actor != null; i++) {
                if (!actions.get(i).act(delta)) complete = false;
                if (actor == null) return true; // This action was removed.
            }
            return complete;
        } finally {
            setPool(pool);
        }
    }

    @Override
    public void restart() {
        complete = false;
        Array<Action3D> actions = this.actions;
        for (int i = 0, n = actions.size; i < n; i++)
            actions.get(i).restart();
    }

    @Override
    public void reset() {
        super.reset();
        actions.clear();
    }

    public void addAction(Action3D action) {
        actions.add(action);
        if (actor != null)
            action.setActor(actor);
    }

    @Override
    public void setActor(Actor3D actor) {
        Array<Action3D> actions = this.actions;
        for (int i = 0, n = actions.size; i < n; i++)
            actions.get(i).setActor(actor);
        super.setActor(actor);
    }

    public Array<Action3D> getActions() {
        return actions;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(64);
        buffer.append(super.toString());
        buffer.append('(');
        Array<Action3D> actions = this.actions;
        for (int i = 0, n = actions.size; i < n; i++) {
            if (i > 0) buffer.append(", ");
            buffer.append(actions.get(i));
        }
        buffer.append(')');
        return buffer.toString();
    }
}
