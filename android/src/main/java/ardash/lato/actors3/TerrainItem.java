package ardash.lato.actors3;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.gdx.scenes.scene3d.Camera3D;

/**
 * A marker interface to mark items that are spread around the terrain and created by the Terraingenerator.
 * All methods in here, are implemented by Actor3D.
 *
 * @author z
 */
public interface TerrainItem extends Cullable {

    void moveBy(float x, float y);

    Tag getTag();

    float getX();

    float getZ();

    void setTag(Tag tag);

    boolean remove();

    void translate(float x, float y, float z);

    @Override
    public default boolean isCulled(Camera3D cam) {
        final float px = Math.abs(Actor3D.getGameScreen().performer.getX());
        final float tx = Math.abs(this.getX());
        final float dx = Math.abs(px - tx);
        if (dx > 80)
            return true;
        return false;
    }

}
