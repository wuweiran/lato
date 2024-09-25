package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pools;

import ardash.gdx.scenes.scene3d.pooling.PoolableActor3D;
import ardash.lato.A;
import ardash.lato.A.ModelAsset;
import ardash.lato.terrain.CollidingTerrainItem;

public class Coin extends PoolableActor3D implements CollidingTerrainItem {
    private boolean hasCollided;
    private Rectangle bb;
    private static Color emissiveLightColor = Color.GOLD.cpy();

    static {
        emissiveLightColor.lerp(Color.BLACK, 0.15f); // make less shiny by moving it 15% towards black
    }

    public Coin() {
        super(getModel());
        setName("coin");
        setScale(0.00203f);
        translate(0, 0.7f, 0);
        setColor(Color.GOLD);
        materials.get(0).set(ColorAttribute.createSpecular(Color.WHITE));
        materials.get(0).set(ColorAttribute.createEmissive(emissiveLightColor));
        materials.get(0).set(FloatAttribute.createShininess(1.0f));
        setRoll(90f);
        setTag(Tag.CENTER);
        init();
        this.bb = new Rectangle(getX(), getY(), 0.1f, 0.1f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        CollidingTerrainItem.super.act(delta);
        setYaw(getYaw() - 3f);
    }

    @Override
    public void draw(ModelBatch modelBatch, Environment environment) {
        super.draw(modelBatch, getStage().dirLightenvironment);
    }

    private static Model getModel() {
        Model m = A.getModel(ModelAsset.YCOIN);
        return m;
    }

    @Override
    public void reset() {
        super.reset();
        hasCollided = false;
        setPosition(0f, 0f);
        translate(0, 0.7f, 0);
    }

    @Override
    public void init() {
        super.init();
        setPitch(MathUtils.random(360f));
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
        remove();
        getGameManager().pickUpCoin();
        hasCollided = true;
        Pools.free(this);
    }

}
