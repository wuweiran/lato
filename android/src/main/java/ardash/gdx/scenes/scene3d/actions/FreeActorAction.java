package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.utils.Pools;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;


/**
 * Frees an actor, if it was in any pool.
 *
 * @author Andreas Redmer
 */
public class FreeActorAction extends Action3D {
    private Actor3D actorToBeFree;
    private boolean freed;

    @Override
    public boolean act(float delta) {
        if (!freed) {
            freed = true;
            if (actorToBeFree != null)
                Pools.free(actorToBeFree);
            if (actor != null)
                Pools.free(actor);
        }
        return true;
    }

    @Override
    public void restart() {
        freed = false;
    }

    @Override
    public void reset() {
        super.reset();
        actorToBeFree = null;
        freed = false;
    }

    public Actor3D getRemoveActor() {
        return actorToBeFree;
    }

    /**
     * Sets the actor to free. If null (the default), the {@link #getActor() actor} will be used.
     */
    public void setFreeActor(Actor3D removeActor) {
        this.actorToBeFree = removeActor;
    }
}
