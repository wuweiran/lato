package ardash.lato.actors;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

import ardash.lato.A;
import ardash.lato.A.ARAsset;
import ardash.lato.actions.MoreActions;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.FogColorChangeListener;
import ardash.lato.weather.SODChangeListener;
import ardash.lato.weather.SkyColorChangeListener;
import ardash.lato.weather.SunColorChangeListener;
import ardash.lato.weather.WeatherProvider;

public class SkyPlane extends Group implements StageAccessor, Disposable, SkyColorChangeListener, SunColorChangeListener, SODChangeListener, FogColorChangeListener {

    private static final float SUN_WIDTH = 2;
    private static final float MIN_STAR_SIZE = 0.1f;
    private static final float MAX_STAR_SIZE = 0.2f;
    private ShapeRenderer sr;
    private Group sunRotor, stars;
    private Image iSunGlow, iSun, iMoonGlow, iMoon;
    private Actor sunFlare, moonFlare;
    private Actor topColorHolder, bottomColorHolder, fogColorHolder;
    private List<SkyPlaneListener> listeners = new LinkedList<SkyPlaneListener>();
    RandomXS128 rand = new RandomXS128(8793246527834L);

    public interface SkyPlaneListener {
        void onSunDirectionChanged(float newAngle);
    }

    public SkyPlane(float width, float height) {
        setSize(width, height);
        final float width2 = getWidth() / 2f;
        final float height2 = getHeight() / 2f;
        moveBy(-width2, 0f);// self center
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        setName("skyplane");
        this.setTouchable(Touchable.childrenOnly);

        stars = new Group(); // to manage general star visiblity and fading
        sunRotor = new Group();
        addActor(stars);
        addActor(sunRotor);
        sunRotor.setPosition(width2, height2); // center on plane
        stars.addAction(Actions.sequence(Actions.fadeOut(0f), Actions.visible(false)));

        // move a bit down, so sun moved behind mountains
        sunRotor.moveBy(0, -15);

        // STARS

        MathUtils.random = rand; // make always the same stars
        for (int i = 0; i < 50; i++) {
            final Image img = new Image(A.getTextureRegion(ARAsset.FOG_PIX));
            final float size = MathUtils.random(MIN_STAR_SIZE, MAX_STAR_SIZE);
            final float ch = MathUtils.randomTriangular(237f, 298f, 237f); // red to blue
//			final float ch = MathUtils.random(55f, 62f); // mostly yellow
            final float cs = MathUtils.randomTriangular(0f, 30f, 0f) / 100f; // more 0
            final float cv = 100f / 100f;

            final float ang = MathUtils.random(0f, 360f);
            final float radius = MathUtils.random(10f, 30f);
            final Group rotor = new Group();
            stars.addActor(rotor);
            rotor.setPosition(sunRotor.getX(), sunRotor.getY());
            rotor.addActor(img);
            img.setWidth(size);
            img.setHeight(size);
            img.getColor().fromHsv(ch, cs, cv);
            img.setPosition(0, radius); // moon rotation radius, stars are above and next to moon
            rotor.setRotation(ang);
            img.setRotation(45f);
            img.setName("star");

            Action ra = Actions.run(new Runnable() {
                @Override
                public void run() {
                    float r = rotor.getRotation();
                    if (r < 0) {
                        r += 360f;
                        rotor.setRotation(r);
                    }
                    if (r > 90 && r < 270) {
                        // makes all stars invisbile that are currently in the bottom half
                        rotor.setVisible(false);
                    } else {
                        rotor.setVisible(true);
                        // apply 2 cos lookups
                        // 1. cos: fades all stars in when they appear from the bottom, and fade out when they set
                        // 2. cosDeg: lets all stars fade in and out every PI*2 degrees. That makes them flicker.
                        rotor.getChild(0).getColor().a = MathUtils.cos(r) * MathUtils.cosDeg(r) * 3;//*radius*1.01f;

                    }
                }
            });
            rotor.addAction(Actions.forever(ra));
            rotor.addAction(Actions.forever(Actions.rotateBy(-360, WeatherProvider.SECONDS_PER_DAY)));
//			img.addAction(Actions.forever(Actions.rotateBy(360,WeatherProvider.SECONDS_PER_DAY)));
        }


        // SUN

        // add sun glow
        iSunGlow = new Image(A.getTextureRegion(ARAsset.GLOW));
        iSunGlow.setWidth(SUN_WIDTH * 26);
        iSunGlow.setHeight(SUN_WIDTH * 26);
        sunRotor.addActor(iSunGlow);
        iSunGlow.setColor(new Color(1, 1, 1, 0.51f)); // adjusting glow intensity here, changes appearance of max fog
        iSunGlow.setName("sunglow");


        // add sun shape
        iSun = new Image(A.getTextureRegion(ARAsset.SUN_SHAPE));
        iSun.setWidth(SUN_WIDTH);
        iSun.setHeight(SUN_WIDTH);
        sunRotor.addActor(iSun);
        iSun.setPosition(0, -20f); // sun rotation radius
        sunFlare = spawnFlareInForeground(iSun, 500f);
        iSun.setTouchable(Touchable.enabled);
        iSun.setName("sunshape");

        // move glow to sun
        iSunGlow.setPosition(iSun.getX() - iSunGlow.getWidth() / 2f, iSun.getY() - iSunGlow.getHeight() / 2f);


        // MOON

        // add moon glow
        iMoonGlow = new Image(A.getTextureRegion(ARAsset.GLOW));
        iMoonGlow.setWidth(SUN_WIDTH * 26);
        iMoonGlow.setHeight(SUN_WIDTH * 26);
        sunRotor.addActor(iMoonGlow);
        iMoonGlow.setColor(new Color(1, 1, 1, 0.1f)); // adjusting glow intensity here, changes appearance of max fog
        iMoonGlow.setName("moonglow");


        // add moon shape
        iMoon = new Image(A.getTextureRegion(ARAsset.MOON_SHAPE));
        iMoon.setWidth(SUN_WIDTH);
        iMoon.setHeight(SUN_WIDTH);
        sunRotor.addActor(iMoon);
        iMoon.setPosition(0, 15f); // moon rotation radius
        moonFlare = spawnFlareInForeground(iMoon, 500f);
        moonFlare.getColor().mul(0.5f);
        moonFlare.setVisible(false);
        iMoon.setTouchable(Touchable.enabled);
        iMoon.setName("moonshape");

        // move moon glow to moon
        iMoonGlow.setPosition(iMoon.getX() - iMoonGlow.getWidth() / 2f, iMoon.getY() - iMoonGlow.getHeight() / 2f);


        // add dummy Actor to hold the color, so the color can be changed by an Action
        topColorHolder = new Actor();
        topColorHolder.setColor(EnvColors.DAY.skyTop);
        bottomColorHolder = new Actor();
        bottomColorHolder.setColor(EnvColors.DAY.skyBottom);
        fogColorHolder = new Actor();
        fogColorHolder.setColor(EnvColors.DAY.fog);
        addActor(topColorHolder);
        addActor(bottomColorHolder);
        addActor(fogColorHolder);

        MathUtils.random = new RandomXS128(); // bring randomness back
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.end();

        Color color = getColor();
        sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);

//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        final Color bc = bottomColorHolder.getColor();
        final Color tc = topColorHolder.getColor();
        final Color fc = fogColorHolder.getColor();
        sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f, bc, bc, fc, fc);
        sr.rect(0, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 10f, fc, fc, tc, tc);
        sr.end();
        Gdx.gl.glLineWidth(1f);
        sr.setColor(Color.WHITE);

//		batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ZERO);
        batch.begin();
        super.draw(batch, parentAlpha);

    }

    @Override
    public void onSkyColorChangeTriggered(Color targetTop, Color targetBottom, float seconds) {
        topColorHolder.addAction(Actions.color(targetTop, seconds));
        bottomColorHolder.addAction(Actions.color(targetBottom, seconds));
    }

    @Override
    public void onFogColorChangeTriggered(Color target, float seconds) {
        fogColorHolder.addAction(Actions.color(target, seconds));
    }

    @Override
    public void onSunColorChangeTriggered(Color target, float seconds) {
        iSunGlow.addAction(MoreActions.noAlphaColor(target, seconds));
        if (!getGameScreen().weather.getCurrentColorSchema().equals(EnvColors.NIGHT))
            sunFlare.addAction(MoreActions.noAlphaColor(target, seconds));
//		imgSun.addAction(Actions.color(target, seconds));
    }

    //	boolean fading = false;
    @Override
    public void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver, EnvColors currentColorSchema) {
        sunRotor.setRotation(percentOfDayOver * -360f);
        for (SkyPlaneListener listener : listeners) {
            listener.onSunDirectionChanged(sunRotor.getRotation());
        }

        // fadeOut doesn't work because the flare is not alpha-blended, it must be faded to black
        if (hourOfDay < 6.5f || hourOfDay > 18.1f)
            sunFlare.setVisible(false);
        else
            sunFlare.setVisible(true);

        if (hourOfDay < 4.9f || hourOfDay > 18.5f) {
            moonFlare.setVisible(true);
//			stars.addAction(Actions.fadeIn(3f));
            stars.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(3f)));
        } else {
            moonFlare.setVisible(false);
            stars.addAction(Actions.sequence(Actions.fadeOut(3f), Actions.visible(false)));
        }

    }

    public void addListener(SkyPlaneListener l) {
        listeners.add(l);
    }

    @Override
    public void dispose() {
        sr.dispose();
    }

}
