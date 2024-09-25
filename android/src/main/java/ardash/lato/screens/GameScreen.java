package ardash.lato.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.Fxaa;
import com.bitfire.utils.ShaderLoader;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.gdx.scenes.scene3d.Camera3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.pooling.PoolsManager;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;
import ardash.lato.A.MusicAsset;
import ardash.lato.GameManager;
import ardash.lato.LatoShaderProvider;
import ardash.lato.LatoStage;
import ardash.lato.LatoStage3D;
import ardash.lato.actors.FlarePlane;
import ardash.lato.actors.MusicProvider;
import ardash.lato.actors.ParticlePlane;
import ardash.lato.actors.Performer;
import ardash.lato.actors.Performer.PerformerListener;
import ardash.lato.actors.Scarf;
import ardash.lato.actors.SkyPlane;
import ardash.lato.actors.SkyPlane.SkyPlaneListener;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors3.MountainRange3;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.WeatherProvider;

public class GameScreen implements Screen {

    //	The world adjusted to MDPI/10 , the smallest expectable device.
    public static float WORLD_WIDTH = 35.59f; // visible meters from left to right, with default zoom on smallest display
    public static float WORLD_HEIGHT = 26.76f;
    public static float SNOWBOARD_LENGTH = 1.85f; // length of snowboard in meters

    /**
     * The actual world-width in the viewport can be taken from Stage.Viewport.WorldWidth
     */
    public static final float MAX_WORLD_WIDTH = WORLD_HEIGHT * (19f / 9f); // on 19/9 display , we don't need to draw farer than this
    public static float CURRENT_WORLD_WIDTH = MAX_WORLD_WIDTH;      // current on display , we don't need to draw farer than this

    public GameManager gm;
    public WeatherProvider weather;
    public LatoStage backStage;
    public LatoStage frontStage;
    public LatoStage guiStage;
    public LatoStage3D mountainStage3d;
    public LatoStage3D stage3d;
    public FlarePlane flarePlane;
    public Performer performer;
    public Scarf scarf;
    public WaveDrawer waveDrawer;
    PerformanceCounter perf;
    float lastPerfOutput = 0;

    public enum LatoShaders {BACK, THREED}

    // Add this class member
    private final GLProfiler profiler;
    private int CURRENT_SCREEN_WIDTH;
    private int CURRENT_SCREEN_SCREEN;

    // post processing:
    private static final boolean isDesktop = (Gdx.app.getType() == ApplicationType.Desktop);
    public PostProcessor postProcessor;

    public GameScreen(GameManager gm) {
        this.gm = gm;
        gm.reset();
        perf = gm.performanceCounters.add("gs");

        // create & enable the profiler
        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();
    }


    /*
     * layers:
     * skyStage (skyPlane)
     * stage3D (MountainRange3)
     * frontStage (ParticlePlane, FlarePlane)
     */
    @Override
    public void show() {
        final float lastKnownHourOfDay = gm.getLastHourOfDay();
        EnvColors lastKnownColorScheme = gm.getLastKnownColorScheme();
        weather = new WeatherProvider(lastKnownHourOfDay <= 0 ? 10.5f : lastKnownHourOfDay, lastKnownColorScheme);
        weather.addSODChangeListener(gm);
        backStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), "bs");
//		stage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), "s");
        frontStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), "fs");
//		frontStage = new LatoStage(new ScreenViewport(), this);
        CURRENT_WORLD_WIDTH = backStage.getViewport().getWorldWidth();
//		guiStage = new LatoStage(Viewports.getDensityAwareViewport(), "gs");
        guiStage = new LatoStage(new ScreenViewport(), "gs");
        final Camera3D mainCam = new Camera3D(30, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mountainStage3d = new LatoStage3D(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, new Camera3D()), getShaderP(LatoShaders.BACK));
        stage3d = new LatoStage3D(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, mainCam), getShaderP(LatoShaders.THREED));

//		backStage.setDebugAll(true);
//		stage.setDebugAll(true);
//		frontStage.setDebugAll(true);
//		backStage3d.setDebug(true, true);
//		Gdx.input.setInputProcessor(backStage);

        guiStage.addActor(weather); // weather can be on any stage
        guiStage.addActor(MusicProvider.getInstance()); // music can be on any stage

        // particle plane must be drawn under flare plane
        final ParticlePlane particlePlane = new ParticlePlane(MAX_WORLD_WIDTH * 2f, WORLD_HEIGHT);
        frontStage.addActor(particlePlane);
        particlePlane.init();
        weather.addPrecipChangeListener(particlePlane);

        // additive flare plane (must be created first, so actors can spawn the flares)
        flarePlane = new FlarePlane(MAX_WORLD_WIDTH * 2f, WORLD_HEIGHT);
        frontStage.addActor(flarePlane);
//		flarePlane.init();
        weather.addSunColorChangeListener(flarePlane);


        final SkyPlane skyPlane = new SkyPlane(MAX_WORLD_WIDTH, WORLD_HEIGHT);
        backStage.addActor(skyPlane);
        weather.addSkyColorChangeListener(skyPlane);
        weather.addFogColorChangeListener(skyPlane);
        weather.addSunColorChangeListener(skyPlane);
        weather.addSODChangeListener(skyPlane);
        weather.addSunColorChangeListener(stage3d);
//    	stage3d.setDirectionalLightColor(Color.YELLOW.cpy());
        skyPlane.addListener(new SkyPlaneListener() {
            @Override
            public void onSunDirectionChanged(float newAngle) {
                Vector2 d = new Vector2().set(1, 1).nor().setAngleDeg(newAngle + 90f);
                Vector3 d3 = new Vector3().set(d.x, d.y, -0.2f).nor();
                stage3d.setDirectionalLightDirection(d3.x, d3.y, d3.z);
            }
        });

        for (int i = 0; i < 4; i++) {
            final int numMountains = 20;
            final MountainRange3 mr = new MountainRange3(numMountains, i * 0.2f + 1.5f);
            mountainStage3d.addActor(mr);
            mr.setName("MountainRange" + i);

            // range offset
            mr.translate(-MountainRange3.MOUNT_SIZE * (i + 1), -5f * i + 2, i * 10);
            mr.setSpeed((i * i + 1) * 0.2f + 1.001f * i);

            // move to center on 0,0
            mr.translate((float) (numMountains / 2) * -MountainRange3.MOUNT_SIZE, 0, 0);

        }

//		backStage.addActor(skyPlane);
//		skyPlane.init();

        performer = new Performer();

        waveDrawer = new WaveDrawer(EnvColors.DAY.ambient);
        performer.addListener(waveDrawer);
        stage3d.addActor(waveDrawer);
        weather.addAmbientColorChangeListener(waveDrawer);
        gm.tm.addListener(waveDrawer);

//		Spruce testTree = new Spruce();
//		testTree.translate(55, 0, 0);
//		backStage3d.addActor(testTree);

        stage3d.addActor(performer);
//		performer.init();
//		p.moveBy(4*1.8f, 10f);
        performer.moveBy(8 * 1.8f, 20f); // initial camera position
        performer.getCamSpot().set(performer.getX(), performer.getY());

        scarf = new Scarf();
        scarf.setTag(Tag.CENTER);
        stage3d.addActor(scarf);


//		performer.enablePhysics();
//		stage.setPerformer(performer); // attach the camera to him
        weather.addAmbientColorChangeListener(performer);


        mountainStage3d.getCamera().update();
        stage3d.getCamera().update();
//		flareStage3d.getCamera().update();

//		stage3d.addActor(new CubeActor3D(1, 1, 1));
//		ModelBuilder mb = new ModelBuilder();
//		stage3d.addActor(new Actor3D(mb.createBox(1, 1, 1, new Material(), 1)));

//        ModelBuilder mb = new ModelBuilder();
//        Model model  = mb.createBox(1, 1, 1, new Material(), 1);
//        model = mb.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
//    			| Usage.Normal);

//        stage3d.addActor(new Actor3D(model));

//        Image3D i3d = new Image3D(40, 40, assets.getSTexture(SceneTexture.PERFORMER), new ModelBuilder(), 20);
//        Image3D i3d = new Image3D(40, 40, Color.WHITE, new ModelBuilder());

//        Triangle3D i3d = new Triangle3D(new Vector3(0, 0, 0), Color.WHITE,new Vector3(10, 0, 0), Color.BLUE,new Vector3(0, 10, 0), Color.BLACK, new ModelBuilder());
//        Circle3D i3d = new Circle3D(10, 20, new Vector3(), new ModelBuilder());
//        stage3d.addActor(i3d);
//        i3d.scale (1f, 0.5f, 0);
//        i3d.scale (40f, 40f, 0);
//
//        skyStage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        skyStage3d.getCamera().lookAt(0, 0, 0);
//        skyStage3d.getCamera().translate(0, 0, 30);
////		skyStage3d.getCamera().moveTo(0, 0, 30, 1f);
//		skyStage3d.getCamera().near = 0.1f;
//		skyStage3d.getCamera().far = 35f;
//		skyStage3d.getCamera().update();

//		stage3d.setPosition(1, 1);
//		stage3d.setScale(10);
//		stage3d.getRoot().setVisible(true);
        mountainStage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mountainStage3d.getCamera().lookAt(0, 0, 0);
//		stage3d.getCamera().moveTo(0, 0, 30, 1f);
        mountainStage3d.getCamera().translate(0, 0, 100);

        mountainStage3d.getCamera().near = 0.1f;
        mountainStage3d.getCamera().far = 110f; // TODO adjust fog intensity here
        mountainStage3d.getCamera().update();

        weather.addFogIntensityChangeListener(mountainStage3d);
        weather.addFogColorChangeListener(mountainStage3d);
        weather.addAmbientColorChangeListener(mountainStage3d);

        stage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage3d.getCamera().lookAt(0, 0, 0);
        stage3d.getCamera().translate(0, 0, 30);
        stage3d.getCamera().near = 0.1f;
        stage3d.getCamera().far = 500f; // TODO adjust fog intensity here
        //((Camera3D)(backStage3d.getCamera())).fieldOfView =90f;
        stage3d.getCamera().update();
        gm.tm.addListener(stage3d);
        weather.addAmbientColorChangeListener(stage3d);

        Vector2 tts = new Vector2(13.5f, 5.566f);
        Image3D titleText = new Image3D(tts.x, tts.y, A.getTextureRegion(ARAsset.TITLESCREEN), new ModelBuilder());
        titleText.setTag(Tag.CENTER);
        titleText.setPosition(performer.getX() - tts.x / 2f + 8f, 8);
        stage3d.addActor(titleText);

//        Toonhouse ma = new Toonhouse();
//        ma.translate(0,0, -2);
//		ma.setTag(Tag.BACK);
//        stage3d.addActor(ma);
        stage3d.setAmbientLightColor(Color.WHITE.cpy());


        final Camera3D cam = (Camera3D) stage3d.getCamera();
        // connect cameras to Performer
        performer.addListener(new PerformerListener() {
            private float lastx, lasty, lastz = 40;

            @Override
            public void onPositionChange(float newX, float newY) {
                lastx = newX;
                lasty = newY;
                if (GameManager.DEBUG_ZOOM_OUT_TO_MAX_SPEED) {
                    if (initZ != null)
                        lastz = initZ + MAX_ZOOM;
                }
//				stage.getCamera().translate(-(stage.getCamera().position.x - performer.getCamSpot().x)
//						, -(stage.getCamera().position.y - performer.getCamSpot().y), 0);
//				stage.getCamera().update();

//				System.out.println("cam move: " + performer.getCamSpot().x +" "+ performer.getCamSpot().y +" "+ lastz);
                cam.moveTo(performer.getCamSpot().x, performer.getCamSpot().y, lastz, 0.3f);
//				cam.moveTo(performer.getX(), performer.getY(), lastz, 0.3f);
                cam.update();

//				Vector2 v00 = new Vector2(0,0);
//				performer.getScarfAttachPoint().localToScreenCoordinates(v00);
                final Vector2 newScarfPosition = performer.getScarfAttachPointInStageCoords();
                scarf.setPosition(performer.getX() + performer.getWidth() / 2f, performer.getY() + performer.getHeight() * 0.4f);
            }

            // the valid zoom interval for the camera to be used to interpolate zooming with current speed
            private static final float MIN_ZOOM = 0f;
            private static final float MAX_ZOOM = 40f;
            Float initZ = null;

            @Override
            public void onSpeedChanged(float newSpeed, float percentage) {
                final float newZoom = MathUtils.lerp(MIN_ZOOM, MAX_ZOOM, percentage);

                if (initZ == null)
                    initZ = cam.getZ();

                lastz = initZ + newZoom;
                // the camera will be moved by onPositionChange()
//				cam.moveTo(lastx, lasty, newz, 0.1f);

                if (performer.getState().isCrashed()) {
                    scarf.setLength(0f);
                } else {
                    scarf.setLength(percentage);
                }
            }

        });

        // add the first piece of terrain
        gm.tm.createNewSection();

        buildGui();
//		stage3d.act(); // act one time, to draw it correctly


        // must be at the end:
        // postprocessing setup
        ShaderLoader.BasePath = "shaders/";
        postProcessor = new PostProcessor(true, false, isDesktop);
        Bloom bloom = new Bloom((int) (Gdx.graphics.getWidth() * 0.25f), (int) (Gdx.graphics.getHeight() * 0.25f));
        postProcessor.addEffect(bloom);
        Fxaa fxaa = new Fxaa(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        postProcessor.addEffect(fxaa);

//        SoundPlayer.swapMusicTo(MusicAsset.BG);
        MusicProvider.getInstance().fadeToMusic(A.getMusic(MusicAsset.BG));
    }

    private ShaderProvider getShaderP(LatoShaders type) {
        DefaultShader.Config config = new DefaultShader.Config();
        config.numDirectionalLights = 1;
        config.numPointLights = 0;
//		config.defaultCullFace = 0;
        config.numBones = 16;
        return new LatoShaderProvider(config, type);
    }

    private void buildGui() {
        Gdx.input.setInputProcessor(guiStage);
        guiStage.setDebugAll(GameManager.DEBUG_GUI);

        if (GameManager.DEBUG_GUI)
            addDebugInfoView();

        Table mainTable = new Table();

        Label distanceLabel = new Label("0m", A.LabelStyleAsset.DISTANCE_LABEL.style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                // we don't use a listener here, because the meters update all the time (each frame)
                setText(performer.getTraveledDistanceMeters() + "m");
            }
        };
        distanceLabel.setAlignment(Align.topRight);
        distanceLabel.setVisible(false);

        Label coinsLabel = new Label(A.getI18NBundle().format("coins", 0), A.LabelStyleAsset.DISTANCE_LABEL.style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                setText(A.getI18NBundle().format("coins", gm.getCoinsPickedUpThisRound()));
            }
        };
        coinsLabel.setAlignment(Align.topLeft);
        coinsLabel.setVisible(false);

        //add labels
        mainTable.setFillParent(true);
        mainTable.row().expandX().fillX().expand().fill();
        mainTable.add(coinsLabel).left().pad(15f * Gdx.graphics.getDensity());
        mainTable.add(distanceLabel).right().pad(15f * Gdx.graphics.getDensity());
        mainTable.row().expandY();

        mainTable.setTouchable(Touchable.enabled);
        mainTable.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                performer.userInput(true);
                // handle the final touch on the game over dialog
                if (performer.getState().isCrashed() && performer.getTimeInState() >= 2f) {
                    gm.reset();
                    gm.game.setScreen(new LoadingScreen(gm));
                } else if (performer.getState().isStarted()) {
                    distanceLabel.setVisible(true);
                    coinsLabel.setVisible(true);
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                performer.userInput(false);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        guiStage.addActor(mainTable);


    }

    private void addDebugInfoView() {
//		lblStyle.font = new BitmapFont();
        Label fps = new Label("fps", A.LabelStyleAsset.DISTANCE_LABEL.style) {
            @Override
            public void act(float delta) {
                super.act(delta);
                final int countTSections = gm.tm.getSections().size();
                final int countTSegments = waveDrawer.getTSLSize();

                String lblText = "fps: " + Gdx.graphics.getFramesPerSecond();
                lblText += "\nactors: " + stage3d.getRoot().getChildren().size;
//				lblText += String.format("\nworld : B %s C %s PC %s", stage3d.world.getBodyCount(), stage3d.world.getContactCount(), performer.currentContacts);
                lblText += "\nposition:" + performer.getX() + " "  + performer.getY();
                lblText += "\nspeed:" + performer.getSpeed() + " " + performer.getSpeedPercentage() * 100f + "%";
                lblText += "\nt-sections: " + countTSections + " t-segments: " + countTSegments;
                lblText += String.format("\nculling : drawn %s of %s", Group3D.draw2Count, Group3D.draw1Count);
                lblText += "\ntime of day: " + weather.currentTOD() + " " + weather.getCurrentPrecip() + " " + weather.getCurrentColorSchema() + " fogginess: " + weather.getCurrentFog();
                setText(lblText);
            }
        };
        Table mainTable = new Table();
//		mainTable.setTouchable(Touchable.enabled);
//		mainTable.addListener(new InputListener() {
//			@Override
//			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//				performer.userInput(true);
////				performer.setSpeed(Performer.MIN_SPEED);
////				gm.setStarted(true);
//				return true;
//			}
//			@Override
//			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				performer.userInput(false);
//				super.touchUp(event, x, y, pointer, button);
//			}
//		});
        guiStage.addActor(mainTable);
        mainTable.setFillParent(true);
        mainTable.row().colspan(3).expandX().fillX();
        mainTable.add(fps);
        mainTable.row().expandY();
//		mainTable.add(img);
        mainTable.add();
        mainTable.row();

        Image pause = new Image(A.getTextureRegion(ARAsset.PAUSE));
        pause.setColor(Color.WHITE.cpy());
        pause.getColor().a = .8f;
//		pause.setSize(10f, 10f);
        mainTable.add(pause).height(40f).width(40f).left();
    }

    @Override
    public void render(float delta) {
        profiler.reset();
        perf.start();
        //draw something nice to look at
//        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
//    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//    	Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(1, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT );
//        Gdx.gl.glDisable(Gdx.gl.GL_DEPTH_TEST);
//        Gdx.gl.glEnable (Gdx.gl.GL_DEPTH_TEST);
//        Gdx.gl.glDepthFunc(Gdx.gl.GL_GREATER);
//        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
//        Gdx.gl20.glClearDepthf(1.0f);
//    	Gdx.gl20.glBlendFunc(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE);
//    	Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);
//
//    	backStage.act(delta);
//    	stage.act(delta);

        // TODO tmp:
//    	Scarf sc = stage.getRoot().findActor("scarf");
//    	sc.setPosition(performer.getX(), performer.getY());

        guiStage.act(delta); // contains weather provider

        backStage.act(delta);
        mountainStage3d.act(delta);
        stage3d.act(delta);
        frontStage.act(delta);

        Group3D.draw1Count = 0;
        Group3D.draw2Count = 0;
        postProcessor.capture();
        backStage.draw();
        mountainStage3d.draw();
        stage3d.draw(true);
        frontStage.draw();
        postProcessor.render();
        guiStage.draw();

        perf.stop();

        float drawCalls = profiler.getDrawCalls();
        float textureBinds = profiler.getTextureBindings();
        final FloatCounter vc = profiler.getVertexCount();
        final int nc = profiler.getCalls();
        final int ss = profiler.getShaderSwitches();


        lastPerfOutput += delta;
        if (GameManager.DEBUG_PRINT_PERFORMANCE_STATS && lastPerfOutput >= 15f) {
            gm.performanceCounters.tick();
            System.out.println(gm.performanceCounters.toString(new StringBuilder()));
            lastPerfOutput = 0;
            System.out.println("dc: " + drawCalls + " tb: " + textureBinds + " vc: " + vc + " ss: " + ss + " nc: " + nc);
        }
        if (GameManager.DEBUG_PRINT_POOL_STATS && lastPerfOutput >= 0.5f) {
            PoolsManager.printStatusOutput();
            lastPerfOutput = 0;
        }

//		float f1 = guiStage.getWidth();
//		float f2 = guiStage.getViewport().getWorldWidth();
//		float f3 = guiStage.getViewport().getWorldHeight();
//		System.out.println("f1: " + f1 + "f2: " + f2 + "f3: " + f3 );
    }

    @Override
    public void resize(int width, int height) {
        backStage.getViewport().update(width, height, false);
        backStage.getCamera().position.set(0f, 0f, 0f); // this cam is centered so we can zoom in/out without moving the sun away from center
//		stage.getViewport().update(width, height, false);
        frontStage.getViewport().update(width, height, false);
        frontStage.getCamera().position.set(0f, 0f, 0f); // this cam is centered so we can zoom in/out without moving the sun away from center
        guiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        postProcessor.rebind();
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        A.dispose();
        backStage.dispose();
        frontStage.dispose();
        guiStage.dispose();
        mountainStage3d.dispose();
        stage3d.dispose();
        postProcessor.dispose();

    }

}
