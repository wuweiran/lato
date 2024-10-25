package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.viewport.Viewport;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.lato.actors.Performer;
import ardash.lato.actors.WaveDrawer;

public class LatoStage extends Stage {

    // the valid zoom interval for the camera to be used to interpolate zooming with current speed
    protected static final float MIN_ZOOM = 1f;
    protected static final float MAX_ZOOM = 2.08f;
    private final String name;
    protected PerformanceCounter pcact;
    protected PerformanceCounter pcdra;
    private Performer performer = null;
    private WaveDrawer waveDrawer = null;

    public LatoStage(Viewport vp, String name) {
        super(vp);
        this.name = name;
        pcact = Actor3D.getGameManager().performanceCounters.add("stage act " + name);
        pcdra = Actor3D.getGameManager().performanceCounters.add("stage dra " + name);
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); // override super constructor
    }

    /**
     * Removes the given actor from the given group, and disposes the actor. Also,
     * if the actor itself is a Group, its children are cleared and disposed
     * recursively.
     *
     * @param actor
     * @param group
     */
    public static void removeAndDispose(Actor actor, Group group) {
        if (actor instanceof Group) {
            clearAndDisposeActors((Group) actor);
        }
        group.removeActor(actor);
        disposeObject(actor);
    }

    public static void clearAndDisposeActors(Group group) {
        for (Actor actor : group.getChildren().items) {
            removeAndDispose(actor, group);
        }
        group.clearChildren();
    }

    public static void disposeObject(Object object) {
        if (object instanceof Disposable disposable) {
            disposable.dispose();
        }
    }

    @Override
    /**
     * to handle inputs
     */
    public void draw() {
        pcdra.start();
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);
        super.draw();
        Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);

//		// performer has moved, the camera shall follow on the y axis
//		if (performer != null)
//		{
////			getCamera().position.y;
////			getCamera().translate(0, -(getCamera().position.y - performer.getY()), 0);
//
//			// point camera to the camspot
//			getCamera().translate(-(getCamera().position.x - performer.getCamSpot().x)
//					, -(getCamera().position.y - performer.getCamSpot().y), 0);
////			getCamera().update();
////			getViewport().apply(false);
//		}


        OrthographicCamera cam = (OrthographicCamera) getCamera();
        if (Gdx.input.isKeyPressed(Keys.Z)) {
            cam.zoom += 0.01f;
            Gdx.app.log("LatoStage", "zoom: " + cam.zoom);
        }
        if (Gdx.input.isKeyPressed(Keys.X)) {
            cam.zoom -= 0.01f;
            if (cam.zoom < 0f) {
                cam.zoom = 0f;
                Gdx.app.log("LatoStage", "zoom: " + cam.zoom);
            }
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            cam.translate(-1.1f, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            cam.translate(1.1f, 0);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            cam.translate(0, 1.1f);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            cam.translate(0, -1.1f);
        }
        if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
            if (performer != null)
                performer.setSpeed(performer.getSpeed() - 1);
        }
        if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
            if (performer != null)
                performer.setSpeed(performer.getSpeed() + 1);
        }

        pcdra.stop();
    }

    @Override
    public void act(float delta) {
        pcact.start();
        super.act(delta);
//		if (waveDrawer != null)
//		{
//			waveDrawer.updateTerrainSegments(performer.getX());
//		}
        pcact.stop();
    }

    public Actor getFirstHitActorAt(float screenX, float screenY) {
        Vector2 tempCoords = new Vector2();
        screenToStageCoordinates(tempCoords.set(screenX, screenY));
        Actor target = hit(tempCoords.x, tempCoords.y, true);
        return target;
    }

//	public WaveDrawer getWaveDrawer() {
//		return waveDrawer;
//	}
//
//	public void setWaveDrawer(WaveDrawer waveDrawer) {
//		this.waveDrawer = waveDrawer;
//	}

    /**
     * Checks if the center of the Actor is covered by any other actor on the stage.
     * This is like a realistic implementation of Actor.isHidden();
     *
     * @param a
     * @return
     */
    public boolean isActorCovered(Actor a) {
        if (a == null)
            throw new RuntimeException("a cannot be null");
        // Check if the first hit actor at the center coordinated is the actor itself. If not: it is covered by another Actor.
        Vector2 tempCoords = new Vector2();
        tempCoords.set(a.getX() + a.getWidth(), a.getY() + a.getHeight());
        a.localToScreenCoordinates(tempCoords);
        tempCoords.x = tempCoords.x;
        tempCoords.y = tempCoords.y;
        Actor firstHitActor = getFirstHitActorAt(tempCoords.x, tempCoords.y);
        if (firstHitActor == null)
            Gdx.app.log("LatoStage", "first hit on [" + tempCoords + "] : NONE");
        else
            Gdx.app.log("LatoStage", "first hit on [" + tempCoords + "] :" + firstHitActor.getName());

        return a != firstHitActor;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        if (this.performer != null)
            throw new RuntimeException("Performer is already set for this stage.");
        this.performer = performer;
    }

    @Override
    public void dispose() {
        // parent implementation clears root, so call that after we disposed all content
        clearAndDisposeActors(getRoot());
        super.dispose();
    }

////	@Override
//	public void onSpeedChanged(float newSpeed, float percentage) {
//		final float newZoom = MathUtils.lerp(MIN_ZOOM, MAX_ZOOM, percentage);
//		OrthographicCamera cam = (OrthographicCamera)getCamera();
////		cam.zoom = newZoom;
//		cam.translate(0, 0, newZoom);
//	}
}
