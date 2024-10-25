package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Pool.Poolable;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;

public class CliffLeft extends Image3D implements TerrainItem, Poolable {

    public CliffLeft(float x, float y) { //, float width, float height) {
        super(getTextureRegion(), getModelBuilder());
        setName("CliffLeft");
        setTag(Tag.MEGAFRONT); // cliffs must be drawn on top of ( in front of the second wave drawer)
        setScale(0.04f, 0.06f, 0.01f);
        setPosition(x, y, 0.01f);
        reset();
        setColor(Color.WHITE); // The other env colors will draw it in the correct shading
    }

    /**
     * -1 for random
     */
    private static AtlasRegion getTextureRegion() {
        return A.getTextureRegion(ARAsset.CLIFF_LEFT);
    }

    private static ModelBuilder getModelBuilder() {
        return new ModelBuilder(); // TODO Pool or reuse a static instance
    }

    @Override
    public void reset() {
    }


}
