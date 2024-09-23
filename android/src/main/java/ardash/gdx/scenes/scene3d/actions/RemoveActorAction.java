package ardash.gdx.scenes.scene3d.actions;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;


/**
 * Removes an actor from the stage.
 *
 * @author Nathan Sweet
 */
public class RemoveActorAction extends Action3D {
    private Actor3D removeActor;
    private boolean removed;

    @Override
    public boolean act(float delta) {
        if (!removed) {
            removed = true;
            (removeActor != null ? removeActor : actor).remove();
        }
        return true;
    }

    @Override
    public void restart() {
        removed = false;
    }

    @Override
    public void reset() {
        super.reset();
        removeActor = null;
    }

    public Actor3D getRemoveActor() {
        return removeActor;
    }

    /**
     * Sets the actor to remove. If null (the default), the {@link #getActor() actor} will be used.
     */
    public void setRemoveActor(Actor3D removeActor) {
        this.removeActor = removeActor;
    }
}
