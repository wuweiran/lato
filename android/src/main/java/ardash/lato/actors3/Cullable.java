package ardash.lato.actors3;

import ardash.gdx.scenes.scene3d.Camera3D;

public interface Cullable {
    public default boolean isCulled(Camera3D cam) {
        return false;
    }

}
