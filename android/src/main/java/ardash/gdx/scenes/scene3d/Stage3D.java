package ardash.gdx.scenes.scene3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.gdx.scenes.scene3d.actions.Actions3D;
import ardash.gdx.scenes.scene3d.actions.ColorAction;
import ardash.gdx.scenes.scene3d.actions.FloatAction;
import ardash.lato.actions.Actions;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.weather.AmbientColorChangeListener;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.FogColorChangeListener;
import ardash.lato.weather.FogIntensityChangeListener;
import ardash.lato.weather.SunColorChangeListener;

public class Stage3D extends InputAdapter implements Disposable,
    FogIntensityChangeListener, FogColorChangeListener, AmbientColorChangeListener, SunColorChangeListener//, //PerformerListener
{
    public static final float MAX_FOG_FAR = 30f;
    public static final float MIN_FOG_FAR = 50f;
    // the valid zoom interval for the camera to be used to interpolate zooming with current speed
    protected static final float MIN_ZOOM = 0f;
    protected static final float MAX_ZOOM = 40f;
    private final ModelBatch modelBatch;
    private final Group3D root;
    public Environment dirLightenvironment = new Environment();
    DirectionalLight directedLightSun = new DirectionalLight().set(1.0f, 1.0f, 1.0f, 0f, -0.8f, -0.2f);
    DirectionalLight directedLightMoon = new DirectionalLight().set(1.0f, 1.0f, 1.0f, 0f, -0.8f, -0.2f);
    boolean directedLightIsSun = true;
    private Environment environment;
    /**
     * A color instance that hold the current fog color. It will be changed and applied to the enviroment every frame.
     */
    private Color fogColor = EnvColors.DAY.fog.cpy();
    /**
     * A color instance that hold the current ambient color. It will be changed and applied to the enviroment every frame.
     */
    private Color ambientColor = EnvColors.DAY.ambient.cpy();
    //    private Camera3D camera;
//    private OrthographicCamera camera;
    private Viewport viewport;
    private FloatAction fodIntensityAction;

    /**
     * Creates a stage with a viewport equal to the device screen resolution. The stage
     * will use its own {@link SpriteBatch}.
     *
     * @param gameScreen
     */
    public Stage3D(Viewport v) {
        this(v, new Environment(), null);
    }

    public Stage3D(Viewport v, ShaderProvider shaderProvider) {
        this(v, new Environment(), shaderProvider);
    }

//    /** Creates a stage with the specified viewport that doesn't keep the aspect ratio.
//     * The stage will use its own {@link SpriteBatch}, which will be disposed when the stage is disposed. */
//    public Stage3D(float width, float height) {
//        this(width, height, new Environment());
//
////        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.14f, 0.94f, 1f));
////        final Color ambient = EnvColors.DAY.ambient;
////        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, ambient.r, ambient.g, ambient.b, 0.51f));
//        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
////        environment.set(new ColorAttribute(ColorAttribute.Fog, 1f, 1f, 1f, 0.1f));
////        final Color fog = EnvColors.DAY.fog;
////        environment.set(new ColorAttribute(ColorAttribute.Fog, fog.r, fog.g, fog.b, 1f));
//
//    }

    public Stage3D(Viewport v, Environment environment, ShaderProvider shaderProvider) {
        root = new Group3D();
        root.setStage(this);

        if (shaderProvider == null)
            modelBatch = new ModelBatch();
        else
            modelBatch = new ModelBatch(shaderProvider);

        this.viewport = v;
        this.environment = environment;

        dirLightenvironment.add(directedLightSun);
    }

    private void setDirectionalLightColor(Color c) {
        directedLightSun.setColor(c);
    }

    public void setDirectionalLightDirection(float x, float y, float z) {
        // turn sun and moon in opposite directions
        Vector3 v3 = Pools.get(Vector3.class).obtain();
        v3.set(x, y, z).nor();
        directedLightSun.setDirection(v3.x, v3.y, v3.z);
        Vector2 v2 = Pools.get(Vector2.class).obtain();
        v2.set(x, y);
        v2.rotateDeg(180);
        directedLightMoon.setDirection(v2.x, v2.y, z);

        // turn alpha down if at bottom of screen, and up if at top of screen, so moon and sun switch all the time smoothly
        // the way to turn off a directional light is to fade it to black
        // all colors must be scaled down anyway, because 2 light sources would overblend
        final float angle = v2.angleDeg();
        // sun movement: dawn -> 180 -> 90 -> 0 -> dusk (invisible otherwise)
        // sun movement: dawn -> 360 -> 270 -> 180 -> dusk (invisible otherwise)
        float sunIntens = 0.001f;
        float moonIntens = 0.001f;
//        directedLightSun.
        if (0 < angle && angle < 180) {
            // show sun
            if (!directedLightIsSun) {
                directedLightIsSun = true;
                dirLightenvironment.add(directedLightSun);
                dirLightenvironment.remove(directedLightMoon);
            }
        } else {
            // show moon
            if (directedLightIsSun) {
                directedLightIsSun = false;
                dirLightenvironment.remove(directedLightSun);
                dirLightenvironment.add(directedLightMoon);
            }
        }

        directedLightSun.color.a = sunIntens;
        directedLightMoon.color.a = moonIntens;
    }

    public void setAmbientLightColor(Color c) {
        ColorAttribute attribute = new ColorAttribute(ColorAttribute.AmbientLight, c.r, c.g, c.b, 0.51f);
//        attribute.color
        environment.set(attribute);
//        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 0.91f));


//		c = c.cpy().lerp(Color.WHITE, 0.1f);
//		c = Color.BLACK.cpy();

        attribute = new ColorAttribute(ColorAttribute.AmbientLight, c.r, c.g, c.b, 0.51f);
        dirLightenvironment.set(attribute);

    }

//    public void setDirectionalLight (Color c)
//    {
//        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//    }

    private void setFogColor(Color c) {
        float fogi = 0;
        if (fodIntensityAction != null)
            fogi = fodIntensityAction.getValue();
        environment.set(new ColorAttribute(ColorAttribute.Fog, c.r, c.g, c.b, fogi)); // TODO remove fog from main stage, is is shown in the debugger
    }

    public void draw() {
        this.draw(false);
    }

    public void draw(boolean in3grounds) {
        Camera camera = viewport.getCamera();
        camera.update();
        if (!root.isVisible()) return;
        modelBatch.begin(camera);
        getModelBatch().setCamera(getCamera());

        if (in3grounds) {
            root.draw(modelBatch, environment, Tag.BACK);
            root.draw(modelBatch, environment, Tag.CENTER);
            root.draw(modelBatch, environment, Tag.FRONT);

            // and if the first Actor was a wave drawer (main game stage), draw it again with an offset
            final Actor3D firstChild = root.getChild(0);
            if (firstChild instanceof WaveDrawer) {
                WaveDrawer wd = (WaveDrawer) firstChild;
                wd.draw(modelBatch, environment, true);
            }

            root.draw(modelBatch, environment, Tag.MEGAFRONT);
            modelBatch.end();
            root.draw(modelBatch, environment, Tag.GIGAFRONT);
            modelBatch.begin(camera);
        } else {
            root.draw(modelBatch, environment);
        }
        modelBatch.end();

        // check user inputs
        if (Gdx.input.isKeyJustPressed(Keys.Z)) {
//        	getCamera().zoom+=1;
//        	camera.zoom+=1;
            camera.translate(0, 0, 1);
        }
        if (Gdx.input.isKeyJustPressed(Keys.X)) {
//        	getCamera().zoom-=1;
//        	camera.zoom-=1;
            camera.translate(0, 0, -1);
        }
    }

    /**
     * Calls {@link #act(float)} with {@link Graphics#getDeltaTime()}.
     */
    public void act() {
        act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    }

    /**
     * Calls the {@link Actor#act(float)} method on each actor in the stage. Typically called each frame. This method also fires
     * enter and exit events.
     *
     * @param delta Time in seconds since the last frame.
     */
    public void act(float delta) {
        root.act(delta);

        // apply the current fog and ambient color
        setFogColor(fogColor);
        setAmbientLightColor(ambientColor);
    }

    /**
     * Adds an actor to the root of the stage.
     *
     * @see Group3D#addActor(Actor3D)
     * @see Actor#remove()
     */
    public void addActor(Actor3D actor) {
        root.addActor(actor);
    }

    /**
     * Adds an action to the root of the stage.
     *
     * @see Group3D#addAction(Action3D)
     */
    public void addAction(Action3D action) {
        root.addAction(action);
    }

    /**
     * Returns the root's child actors.
     *
     * @see Group#getChildren()
     */
    public Array<Actor3D> getActors() {
        return root.getChildren();
    }

    /**
     * Adds a listener to the root.
     *
     * @see Actor#addListener(EventListener)
     */
    public boolean addListener(Event3DListener listener) {
        return root.addListener(listener);
    }

    /**
     * Removes a listener from the root.
     *
     * @see Actor#removeListener(EventListener)
     */
    public boolean removeListener(Event3DListener listener) {
        return root.removeListener(listener);
    }

    /**
     * Removes the root's children, actions, and listeners.
     */
    public void clear() {
        root.clear();
    }

    public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Camera getCamera() {
        Camera camera = viewport.getCamera();
        return camera;
    }

//    /** Sets the stage's camera. The camera must be configured properly. {@link Stage#draw()} will call {@link Camera#update()} and use the {@link Camera#combined} matrix
//     * for the SpriteBatch {@link SpriteBatch#setProjectionMatrix(com.badlogic.gdx.math.Matrix4) projection matrix}. */
//    public void setCamera (OrthographicCamera camera) {
//        this.camera = camera;
//    }

    /**
     * Returns the root group which holds all actors in the stage.
     */
    public Group3D getRoot() {
        return root;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Actor3D getObject(int screenX, int screenY) {
        Actor3D temp = null;
        SnapshotArray<Actor3D> children = root.getChildren();
        Actor3D[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            temp = hit(screenX, screenY, actors[i]);
            if (actors[i] instanceof Group3D)
                temp = hit(screenX, screenY, (Group3D) actors[i]);
        }
        children.end();
        return temp;
    }

    public Actor3D hit(int screenX, int screenY, Actor3D actor3D) {
        throw new RuntimeException("not implemented");
//        Camera camera = viewport.getCamera();
//        Ray ray = camera.getPickRay(screenX, screenY);
//        final float dist2 = actor3D.intersects(ray);
//        if (dist2 >= 0) {
//            return actor3D;
//        }
//        return null;
    }

    public Actor3D hit(int screenX, int screenY, Group3D group3d) {
        Actor3D temp = null;
        SnapshotArray<Actor3D> children = group3d.getChildren();
        Actor3D[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            temp = hit(screenX, screenY, actors[i]);
            if (actors[i] instanceof Group3D)
                temp = hit(screenX, screenY, (Group3D) actors[i]);
        }
        children.end();
        return temp;
    }

    /**
     * If true, {@link Actor3D#drawDebug(ModelBatch, Environment)} will be called for this group and, optionally, all children recursively.
     */
    public void setDebug(boolean enabled, boolean recursively, ModelBuilder modelBuilder) {
        root.setDebug(enabled, recursively, modelBuilder);
    }

    public void setDebug(boolean enabled, boolean recursively) {
        root.setDebug(enabled, recursively);
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        root.dispose();
        clear();
    }

    @Override
    public void onFogIntensityChanged(float currentFog, float newIntensity, float duration) {
        fodIntensityAction = Actions.floata(currentFog, newIntensity, duration);
        addAction(fodIntensityAction);
//		newIntensity = MathUtils.clamp(newIntensity, 0.0f, 1.0f);
//		final float newFarValue = MathUtils.lerp(MIN_FOG_FAR, MAX_FOG_FAR, newIntensity);
//		FloatAction action = new FloatAction(getCamera().far, newFarValue, duration) {
//			@Override
//			protected void update(float percent) {
//				super.update(percent);
//				actor.getStage().getCamera().far = getValue();
//				actor.getStage().getCamera().update();
//			}
//		};
//		addAction(action);
//		fogColor.a = newIntensity;
    }

    @Override
    public void onFogColorChangeTriggered(Color target, float seconds) {
        final ColorAction action = Actions.noAlphaColor(target, seconds);
        action.setColor(fogColor);
        addAction(action);
    }

    @Override
    public void onAmbientColorChangeTriggered(Color target, float seconds) {
//		target = target.cpy().mul(1.5f); // apply ambience a bit brighter
//		target.set(Color.WHITE);
        final ColorAction action = Actions3D.color(target, seconds);
        action.setColor(ambientColor);
        addAction(action);
    }

//	Float initZ = null;
//	@Override
//	public void onSpeedChanged(float newSpeed, float percentage) {
//		final float newZoom = MathUtils.lerp(MIN_ZOOM, MAX_ZOOM, percentage);
//		Camera3D cam = (Camera3D)getCamera();
//
//		if (initZ == null)
//			initZ = cam.getZ();
//
////		cam.translate(0, 0, initZ - newZoom);
////		cam.position.z = initZ + newZoom;
////		cam.update();
//
////		cam.moveTo(cam.getX(), cam.getY(), initZ+newZoom, 3f);
//	}

    @Override
    public void onSunColorChangeTriggered(Color target, float seconds) {
        Color c = target.cpy().lerp(Color.BLACK, 0.1f);
        final ColorAction action = Actions.noAlphaColor(c, seconds);
        action.setColor(directedLightSun.color);
        addAction(action);
    }

}
