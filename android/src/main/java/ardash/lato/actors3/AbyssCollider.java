package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Pool.Poolable;

import ardash.gdx.scenes.scene3d.Camera3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;
import ardash.lato.actors.Performer;
import ardash.lato.actors.Performer.Demise;

public class AbyssCollider extends Image3D implements TerrainItem, Poolable {

    boolean hasCollided;

    public AbyssCollider(float x, float y, float width, float height) {
        super(width, height, getTextureRegion(), getModelBuilder());
        setName("AbyssCollider");
        setTag(Tag.MEGAFRONT); // abyss (center of canyon) is always on center, not in background of foreground
//		setScale(0.02f, 0.02f, 1);
        setPosition(x, y, 5f);
        reset();
        setColor(Color.PINK);
        setVisible(false);
    }

    @Override
    public void draw(ModelBatch modelBatch, Environment environment) {
//		nothing
    }

    @Override
    public void reset() {
        hasCollided = false;
    }

    /**
     * -1 for random
     */
    private static AtlasRegion getTextureRegion() {
        return A.getTextureRegion(ARAsset.FOG_PIX);
    }

    private static ModelBuilder getModelBuilder() {
        return new ModelBuilder(); // TODO Pool or reuse a static instance
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // by doing collision detection here we reduce the work and the code needed in Performer (ie. if there is no Stone, nothing need to be done))
        detectCollision();
    }

    private void detectCollision() {
        if (hasCollided)
            return;

        final float myWidth = getWidth();
        final float pWidth = 1.0f;
        final float myX = getX();
        final Performer performer = getGameScreen().performer;
        final float pX = performer.getX();

        if ((pX > myX) && ((pX + pWidth) < (myX + myWidth))) {

            // check Y coords too
            final float stoneYtop = getY() + getHeight();
            final float pYbottom = performer.getY();

//			System.out.println("pYbottom " + pYbottom + " stoneYtop " + stoneYtop );
            if (stoneYtop > pYbottom) {
                performer.setCauseOfDeath(Demise.DROP_IN_CANYON);
                performer.drop();
                hasCollided = true;
            }
        }
    }

    @Override
    public boolean isCulled(Camera3D cam) {
        return true;
    }
}
