package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.utils.Pool;

import ardash.gdx.scenes.scene3d.Action3D;

/**
 * Executes a number of actions one at a time.
 *
 * @author Nathan Sweet
 */
public class SequenceAction extends ParallelAction {
    private int index;

    public SequenceAction() {
    }

    public SequenceAction(Action3D action1) {
        addAction(action1);
    }

    public SequenceAction(Action3D action1, Action3D action2) {
        addAction(action1);
        addAction(action2);
    }

    public SequenceAction(Action3D action1, Action3D action2, Action3D action3) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
    }

    public SequenceAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
        addAction(action4);
    }

    public SequenceAction(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
        addAction(action4);
        addAction(action5);
    }

    @Override
    public boolean act(float delta) {
        if (index >= actions.size) return true;
        Pool<Action3D> pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            if (actions.get(index).act(delta)) {
                if (actor == null) return true; // This action was removed.
                index++;
                return index >= actions.size;
            }
            return false;
        } finally {
            setPool(pool);
        }
    }

    @Override
    public void restart() {
        super.restart();
        index = 0;
    }
}
