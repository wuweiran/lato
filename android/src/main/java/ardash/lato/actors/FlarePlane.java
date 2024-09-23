package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;
import ardash.lato.LatoStage;
import ardash.lato.weather.SunColorChangeListener;

public class FlarePlane extends Group implements StageAccessor, Disposable, SunColorChangeListener {

    private static final float SUN_WIDTH = 2;
//	private ShapeRenderer sr;

    public FlarePlane(float width, float height) {
        setSize(width, height);
//		moveBy(getWidth()/-2f, 0f);// self center
//		sr = new ShapeRenderer();
//		sr.setAutoShapeType(true);
        this.setName("flareplane");
    }


    public Actor spawnFlare(Actor emitter, float size) {
        Image imgGlow = new Image(A.getTextureRegion(ARAsset.ADD_FLARE));
        imgGlow.setUserObject(emitter);
        final float width = SUN_WIDTH * 26;
        imgGlow.setWidth(width);
        imgGlow.setHeight(width);
        addActor(imgGlow);
        imgGlow.setPosition(emitter.getX() - emitter.getWidth() / 2f, emitter.getY() - emitter.getHeight() / 2f);
        imgGlow.moveBy(-width / 2f, -width / 2f); // move to own origin
        return imgGlow;
    }

    public Actor spawnFlare(Actor3D emitter, float size) {
        Image imgGlow = new Image(A.getTextureRegion(ARAsset.ADD_FLARE));
        imgGlow.setUserObject(emitter);
        final float width = SUN_WIDTH * 26;
        imgGlow.setWidth(width);
        imgGlow.setHeight(width);
        addActor(imgGlow);
        imgGlow.setPosition(emitter.getX() - emitter.getWidth() / 2f, emitter.getY() - emitter.getHeight() / 2f);
        imgGlow.moveBy(-width / 2f, -width / 2f); // move to own origin
        return imgGlow;
    }

    @Override
    public void act(float delta) {
        // update all flares
        for (Actor a : getChildren()) // iterate all flares in this group
        {
            if (a instanceof Image) {
                Image flare = (Image) a;

                // handle 2D emitters
                if (flare.getUserObject() instanceof Actor) {
                    Actor emitter = ((Actor) flare.getUserObject());
                    Vector2 tmpV = new Vector2();
                    tmpV.set(emitter.getX() + emitter.getWidth() / 2f, emitter.getY() + emitter.getHeight() / 2f);
                    emitter.getParent().localToStageCoordinates(tmpV);
                    flare.getParent().stageToLocalCoordinates(tmpV);
                    flare.setPosition(tmpV.x - flare.getWidth() / 2f, tmpV.y - flare.getHeight() / 2f);

                    // hide flare if emitter covered by another actor
                    if (emitter.getStage() != null) {
                        if (emitter.getStage() instanceof LatoStage) {
                            LatoStage st = (LatoStage) emitter.getStage();
//							if (st.isActorCovered(emitter))
                            {
//								flare.setVisible(!st.isActorCovered(emitter));
                            }
                        }
                    }
                }

                // handle 3D emitters
                if (flare.getUserObject() instanceof Actor3D) {
                    Actor3D emitter = ((Actor3D) flare.getUserObject());
                    Vector2 tmpV = new Vector2();
//					tmpV.set(emitter.getX()+emitter.getWidth()/2f, emitter.getY()+emitter.getHeight()/2f);
                    emitter.localToScreenCoordinates(tmpV);
//					emitter.getStage().getCamera().vi
//					tmpV.set(200,200);
                    tmpV.add(Gdx.graphics.getWidth() / 2f, +Gdx.graphics.getHeight() / 2f);
                    flare.getParent().screenToLocalCoordinates(tmpV);
                    tmpV.scl(1f, -1f);
//					tmpV.scl(0.5f);
//					tmpV.set(0,0);

                    // this doesn't work because the 2d stage cam is orthogonal, the other is perspective
                    flare.setPosition(tmpV.x - flare.getWidth() / 2f, tmpV.y - flare.getHeight() / 2f);
//					flare.setPosition(tmpV.x, tmpV.y);

                    // hide flare if emitter covered by another actor
                    if (emitter.getStage() != null) {
//						if (emitter.getStage() instanceof LatoStage) {
//							LatoStage st = (LatoStage) emitter.getStage();
////							if (st.isActorCovered(emitter))
//							{
////								flare.setVisible(!st.isActorCovered(emitter));
//							}
//						}
                    }
                }

            }
        }
        super.act(delta);
    }

    @Override
    public void onSunColorChangeTriggered(Color target, float seconds) {
        // TODO Auto-generated method stub
        // TODO continue here: subscribe only the flare of the sun, in the class when it spawns
    }

    @Override
    /**
     * Draw this actor with additive blending.
     */
    public void draw(Batch batch, float parentAlpha) {
        batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
//		Disposables.gracefullyDisposeOf(sr);
    }


}
