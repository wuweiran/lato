package ardash.lato.actors3;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.SpriteGroupAsset;
import ardash.lato.actors.Performer;
import ardash.lato.actors.Performer.Demise;
import ardash.lato.actors.Performer.Pose;
import ardash.lato.terrain.CollidingTerrainItem;

public class Stone extends Image3D implements CollidingTerrainItem, Poolable {

    private boolean hasCollided;
    private Rectangle bb;

    public Stone() {
        this(-1);
    }

    public Stone(int stoneIndex) {
        super(getTextureRegion(stoneIndex), getModelBuilder());
        setName("Stone");
        setTag(Tag.CENTER); // stones are always on center, not in background of foreground
        setScale(0.02f, 0.02f, 1);
        reset();
        this.bb = new Rectangle(getX(), getY(), 1, 1);
    }

    @Override
    public void reset() {
        hasCollided = false;
    }

    /**
     * -1 for random
     */
    private static AtlasRegion getTextureRegion(int stoneIndex) {
        if (stoneIndex == -1) {
            return A.getRandomAtlasRegion(SpriteGroupAsset.STONE);
        }
        return A.getTextureRegions("stone").get(stoneIndex);
    }

    private static ModelBuilder getModelBuilder() {
        return new ModelBuilder(); // TODO Pool or reuse a static instance
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        CollidingTerrainItem.super.act(delta);
    }

    public void detectCollision() {
        if (hasCollided)
            return;

        // TODO update bb only when position changes (and in init())
        bb.setPosition(getX(), getY());
        if (getGameScreen().performer.bb.overlaps(bb)) {
            onCollision();
        }
    }

    public void onCollision() {
        final Performer performer = getGameScreen().performer;
        if (performer.getState().isInAir()) {
            performer.setCauseOfDeath(Demise.LAND_ON_STONE);
        } else {
            performer.setCauseOfDeath(Demise.HIT_STONE);
        }
        performer.crash(Pose.CRASH_NOSE);
        hasCollided = true;
    }
}
