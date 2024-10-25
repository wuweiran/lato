package ardash.lato.terrain;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.gdx.scenes.scene3d.Camera3D;

public class DummyTerrainItem implements CollidingTerrainItem {

    @Override
    public void moveBy(float x, float y) {
        // nothing
    }

    @Override
    public Tag getTag() {
        return null;
    }

    @Override
    public void setTag(Tag tag) {
        // nothing
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getZ() {
        return 0;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public void translate(float x, float y, float z) {
        // nothing
    }

    @Override
    public boolean isCulled(Camera3D cam) {
        return true;
    }

    @Override
    public void detectCollision() {
        // nothing
    }

    @Override
    public void onCollision() {
        // nothing
    }

}
