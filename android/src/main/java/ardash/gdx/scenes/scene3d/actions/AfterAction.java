package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.utils.Array;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * Executes an action only after all other actions on the actor at the time this action was added have finished.
 *
 * @author Nathan Sweet
 */
public class AfterAction extends DelegateAction {
    private final Array<Action3D> waitForActions = new Array<>(false, 4);

    @Override
    public void setActor(Actor3D actor) {
        if (actor != null) waitForActions.addAll(actor.getActions());
        super.setActor(actor);
    }

    @Override
    public void restart() {
        super.restart();
        waitForActions.clear();
    }

    @Override
    protected boolean delegate(float delta) {
        Array<Action3D> currentActions = actor.getActions();
        if (currentActions.size == 1) waitForActions.clear();
        for (int i = waitForActions.size - 1; i >= 0; i--) {
            Action3D action = waitForActions.get(i);
            int index = currentActions.indexOf(action, true);
            if (index == -1) waitForActions.removeIndex(i);
        }
        return waitForActions.size <= 0 && action.act(delta);
    }
}
