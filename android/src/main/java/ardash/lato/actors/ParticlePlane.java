package ardash.lato.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

import ardash.lato.A;
import ardash.lato.A.ParticleAsset;
import ardash.lato.weather.PrecipitationChangeListener;
import ardash.lato.weather.WeatherProvider.Precipitation;

public class ParticlePlane extends Group implements StageAccessor, Disposable, PrecipitationChangeListener {
    ParticleEffect rainEffect = new ParticleEffect();
    ParticleEffect snowEffect = new ParticleEffect();

    public ParticlePlane(float width, float height) {
        setSize(width, height);
//		moveBy(getWidth()/-2f, 0f);// self center
        this.setName("particleplane");
    }

    //	@Override
    public void init() {
        rainEffect = A.getParticleEffect(ParticleAsset.RAIN);
        snowEffect = A.getParticleEffect(ParticleAsset.SNOW);
//		TextureAtlas ta = getAssetManager().get(Assets.SCENE_ATLAS);
//		rainEffect.load( Gdx.files.internal("rain.p"), ta);
        rainEffect.scaleEffect(0.05f);
        rainEffect.setPosition(-22f, 20f);
//		rainEffect.start();

//		snowEffect.load( Gdx.files.internal("snow.p"), ta);
        snowEffect.scaleEffect(0.08f);
        snowEffect.setPosition(-23f, 20f);
        snowEffect.start();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        rainEffect.update(delta);
        snowEffect.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//		batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE);
//		super.draw(batch, parentAlpha);
        rainEffect.draw(batch);
        snowEffect.draw(batch);
    }

    public void startRain(float duration) {
        rainEffect.getEmitters().get(0).duration = duration;
        rainEffect.getEmitters().get(0).durationTimer = 0;
        rainEffect.getEmitters().get(0).getDuration().setLow(duration * 1000f);
        rainEffect.start();
    }

    public void startSnow(float duration) {
        snowEffect.getEmitters().get(0).duration = duration;
        snowEffect.getEmitters().get(0).durationTimer = 0;
        snowEffect.getEmitters().get(0).getDuration().setLow(duration * 1000f);
        snowEffect.start();
    }

    @Override
    public void dispose() {
        rainEffect.dispose();
        snowEffect.dispose();
    }

    @Override
    public void onPrecipitationChanged(Precipitation targetPrecipitation, float seconds) {
        switch (targetPrecipitation) {
            case RAIN:
                startRain(seconds);
                break;
            case SNOW:
                startSnow(seconds);
                break;
            default:
                break;
        }
    }


}
