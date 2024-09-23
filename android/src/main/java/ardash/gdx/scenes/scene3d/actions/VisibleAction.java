package ardash.gdx.scenes.scene3d.actions;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * Sets the actor's {@link Actor3D#setVisible(boolean) visibility}.
 *
 * @author Nathan Sweet
 */
public class VisibleAction extends Action3D {
    private boolean visible;

    @Override
    public boolean act(float delta) {
        actor.setVisible(visible);
        return true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
