package ardash.gdx.scenes.scene3d.actions;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

public class AddAction extends Action3D {
    private Actor3D target;
    private Action3D action;

    @Override
    public boolean act(float delta) {
        (target != null ? target : actor).addAction(action);
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

    public void restart() {
        if (action != null) action.restart();
    }

    @Override
    public void reset() {
        super.reset();
        target = null;
        action = null;
    }
}
