package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Pool.Poolable;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;

public class AbyssMist extends Image3D implements TerrainItem, Poolable {
    public static final ModelBuilder mb = new ModelBuilder();

    boolean hasCollided;

    public AbyssMist(float x, float y, float width, float height) {
        super(width, height, getTextureRegion(), mb);
        setName("AbyssMist");
        setTag(Tag.MEGAFRONT); // abyss (center of canyon) is always on center, not in background of foreground
        setScale(1f, 1f, 0.01f);
        setPosition(x, y, 10.051f);
        reset();
        setColor(Color.WHITE);
    }

    /**
     * -1 for random
     */
    private static AtlasRegion getTextureRegion() {
        return A.getTextureRegion(ARAsset.GLOW);
    }

    @Override
    public void reset() {
        hasCollided = false;
    }

}
