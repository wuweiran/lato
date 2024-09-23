package ardash.lato.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.AdvShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import ardash.gdx.scenes.scene3d.Group3D;
import ardash.lato.GameManager;
import ardash.lato.actions.MoreActions;
import ardash.lato.actors.Performer.PerformerListener;
import ardash.lato.terrain.Downer;
import ardash.lato.terrain.Section;
import ardash.lato.terrain.TerrainManager.TerrainListener;
import ardash.lato.terrain.TerrainSegList;
import ardash.lato.weather.AmbientColorChangeListener;
import ardash.lato.weather.EnvColors;

public class WaveDrawer extends Group3D implements Disposable, AmbientColorChangeListener, PerformerListener, TerrainListener {
    /**
     * Everything that is more than this far behind the Performer can be deleted form the stage
     */
    public static final float PASSED_TERRAIN = 200f; // longest possible terrain * 2 (if cull to closely then cliffs are not attached to the corners correctly.)

    /**
     * If there is no terrain this much in front of the Performer, new Terrain should be created.
     * Must be at least the size of the longest Section. Making it too long will degrade performance.
     */
    public static final float FUTURE_TERRAIN = 200f;

    /**
     * The size of a step. The amount of meters to move forward to draw the next terrain segment.
     */
    public static final float DRAW_STEPS = 0.5f; // 8.8 good with edges of abyss

    private final ModelBuilder modelBuilder = new ModelBuilder();
    private AdvShapeRenderer sr;
    TerrainSegList terrainSegmentList;
    Vector2 tmpVector = new Vector2(); // can be used by one method atomically
    private Actor ambientColorContainer = new Actor();

    /**
     * list to be used in the draw method to collect points
     */
    private List<Float> polygonPoints = new ArrayList<Float>(100);

    public WaveDrawer(Color color) {
        setName("WaveDrawer");
        setTag(Tag.CENTER);
//		setColor(color);
        sr = new AdvShapeRenderer();
        sr.setAutoShapeType(true); // TODO check what types we draw

//sr.translate(0, 0, 20);
        // path setup
        terrainSegmentList = new TerrainSegList();
//		final HomeHill homehill = new HomeHill();
//		terrainSegmentList.addAllNoOffset(homehill); // TODO init starting area of terrain
//		getGameManager().tm.createNewSection(terrainSegmentList.last());
        // call listener manually
//		getGameManager().getGameScreen().stage3d.onNewSectionCreated(homehill);

//		Image3D img = new Image3D(1.85f, 10, Color.GOLD, null, modelBuilder, 0);
//		addActor(img);
//		img = new Image3D(10, 10, Color.GOLD, null, modelBuilder, -5);
//		addActor(img);
        ambientColorContainer.setColor(EnvColors.DAY.ambient.cpy());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        ambientColorContainer.act(delta);
    }

    @Override
    public void draw(ModelBatch batch, Environment environment) {
        this.draw(batch, environment, false);
    }

    /**
     * @param batch
     * @param environment
     * @param drawOffset  true, to draw the waves with a predefined offset
     */
    public void draw(ModelBatch batch, Environment environment, boolean drawOffset) {
        super.draw(batch, environment);

        final float offsetY = drawOffset ? -5 : 0;
//		final float offsetY = drawOffset ? 5 : 10;
        batch.end();

        sr.begin();
        if (GameManager.DEBUG_VIEW) {
            sr.set(ShapeType.Line);
        } else {
            sr.set(ShapeType.Filled);
        }

        sr.setColor(ambientColorContainer.getColor());
        if (drawOffset)
            sr.setColor(ambientColorContainer.getColor().cpy().mul(0.9f));

        sr.setProjectionMatrix(getStage().getCamera().combined);
//

        float performerY = getGameScreen().performer.getY();
        int counter = 0;
        long startTime = System.currentTimeMillis();
        float firstX = MathUtils.ceil(terrainSegmentList.first().x);
        float lastX = terrainSegmentList.last().x;
        polygonPoints.clear();
        for (float x = firstX; x < lastX - DRAW_STEPS; x += DRAW_STEPS) {
            float toX = x - DRAW_STEPS;
//			float toY = terrainSegmentList.heightAt(toX);

//			System.out.println("campos: " + getStage().getCamera().position);
            // culling based on X value. Y value is just the current Y of the performer
            // don't cull if performer is dropping
            if (getGameScreen().performer.state != PlayerState.DROPPED && !getGameScreen().performer.state.isCrashed()) // TODO this check is easier, move up ??
                if (!getStage().getCamera().frustum.pointInFrustum(x - DRAW_STEPS * 2f, performerY, 0)
                    && !getStage().getCamera().frustum.pointInFrustum(x + DRAW_STEPS * 2f, performerY, 0)) {
                    continue;
                }

            float y = terrainSegmentList.heightAt(x);
            polygonPoints.add(x);
            polygonPoints.add(y + offsetY);

//			float[] fa = {x,y, toX,toY, toX,y-500f, x, y-500f};
//			sr.polygon(fa);
            counter++;
        }
        if (!polygonPoints.isEmpty()) {
            // close polygon
            polygonPoints.add(polygonPoints.get(polygonPoints.size() - 2));
            polygonPoints.add(polygonPoints.get(polygonPoints.size() - 2) - 500f);
            polygonPoints.add(firstX);
            polygonPoints.add(polygonPoints.get(polygonPoints.size() - 2) - 500f);


            // convert and draw
            float[] fa = new float[polygonPoints.size()];
            int i = 0;
            for (Float f : polygonPoints) {
                fa[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
            }

            sr.polygon(fa);
        }

        long endTime = System.currentTimeMillis() + 1;
        long drawTime = endTime - startTime;
//		System.out.println(String.format("%f PPS. Drawn %d in %d ms", (float)counter/(float)drawTime, counter, drawTime));
//
        sr.end();

        batch.begin(getStage().getCamera());

        if (Gdx.input.isKeyJustPressed(Keys.T)) {
            terrainSegmentList.addAll(new Downer());
        }
    }

    public float getHeightAt(final float x) {
        return terrainSegmentList.heightAt(x);
    }

    /**
     * for performance reasons this one returns the angle at the x of the last call of getHeightAt(x)
     */
    public float getAngleAtX(final float x) {
        tmpVector.set(x, getHeightAt(x));
        tmpVector.sub(x + 0.1f, getHeightAt(x + 0.1f));
        float angle = tmpVector.scl(-1f).angle();
//		angle = MathUtils.clamp(angle, -85f, 85f);
//		System.out.println(angle);
        return angle;
    }

    @Override
    public void moveBy(float x, float y) {
        super.moveBy(x, y);
        sr.translate(x, y, 0);

    }

    @Override
    public void dispose() {
        sr.dispose();
    }

    /**
     * Remove passed terrain segments and add new terrain in front of the player.
     *
     * @param x The current position of the performer
     */
    private void updateTerrainSegments(float x) {
        // don't cull if performer is dropping
        if (getGameScreen().performer.state == PlayerState.DROPPED || getGameScreen().performer.state.isCrashed())
            return;

        final float currentMin = terrainSegmentList.first().x;
        final float currentMax = terrainSegmentList.last().x;

        if (currentMin + PASSED_TERRAIN < x) {
            // old terrain can be removed
            terrainSegmentList.removeFirst();
        }
        if (currentMax - FUTURE_TERRAIN < x) {
            // new terrain must be added
//			terrainSegmentList.addAll(new Downer());
            getGameManager().tm.createNewSection();
//			terrainSegmentList.addAll(getGameManager().tm.getLastSection());
//			terrainSegmentList.addAllNoOffset(getGameManager().tm.getLastSection());

        }

    }

//	@Override
//		public boolean remove() {
//			throw new RuntimeException("ERROR: don't remove the wave drawer!");
//		}

    /**
     * Returns the size of the terrain-segment-list - ONLY for debugging purposes (to show in debug window label).
     */
    public int getTSLSize() {
        return terrainSegmentList.size();
    }

    @Override
    public void onAmbientColorChangeTriggered(Color target, float seconds) {
//		addAction(Actions.color(ambientColorContainer.getColor(), seconds));
        ambientColorContainer.addAction(MoreActions.noAlphaColor(target, seconds));
    }

    @Override
    public void onPositionChange(float newX, float newY) {
        updateTerrainSegments(newX);
    }

    @Override
    public void onNewSectionCreated(Section s) {
        terrainSegmentList.addAllNoOffset(getGameManager().tm.getLastSection());
    }

    @Override
    public void onSpeedChanged(float newSpeed, float percentage) {
        // nothing !!
    }
}
