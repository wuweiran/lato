

package ardash.lato.weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;

import ardash.lato.GameManager;
import ardash.lato.actions.MoreActions;

public class WeatherProvider extends Actor {

    public enum Precipitation {
        RAIN, SNOW, FOG, CLEAR; // STORM is a sub-mode of RAIN
    }

    public static final boolean FASTMODE = GameManager.DEBUG_WEATHER_FASTMODE;
    public static final float DAYTIME_HOURS = 16f;
    public static final float NIGHT_HOURS = 8f;
    public static final float DAY_HOURS = DAYTIME_HOURS + NIGHT_HOURS;
    public static final float DUSK_HOURS = 1.84f;
    public static final float DAWN_HOURS = 0.92f;
    public static final float SECONDS_PER_DAY = FASTMODE ? 180f : 7f * 60f + 20f; // one 24 hours cycle shall have 7 minutes and 20 seconds (only 180 seconds in FASTMODE)
    public static final float SECONDS_PER_HOUR = SECONDS_PER_DAY / DAY_HOURS;
    public static final float DAYTIME_SECONDS = DAYTIME_HOURS * SECONDS_PER_HOUR;
    public static final float NIGHT_SECONDS = NIGHT_HOURS * SECONDS_PER_HOUR;
    public static final float DUSK_SECONDS = DUSK_HOURS * SECONDS_PER_HOUR;
    public static final float DAWN_SECONDS = DAWN_HOURS * SECONDS_PER_HOUR;
    public static final float MIN_FOG = 0.0175f;
    public static final float MAX_FOG = 0.04f;
    public static final float MAX_FOG_NO_PRECIPITATION = (MAX_FOG + MIN_FOG) / 2f;
    public static final float FOG_STEPS = (MAX_FOG - MIN_FOG) / 20f; // for calibration with keyboard

    /**
     * current Second Of Day. A value from 0 to 24 * SECONDS_PER_HOUR
     */
    float currentSOD; // 10.5 = 10:30 am
    EnvColors currentColorSchema;
    float currentFog = MIN_FOG;
    Precipitation currentPrecip = Precipitation.CLEAR;
    FloatAction currentPrecipAction = null;

    private final List<AmbientColorChangeListener> ambientColorChangeListeners = new ArrayList<>(4);
    private final List<FogColorChangeListener> fogColorChangeListeners = new ArrayList<>(4);
    private final List<FogIntensityChangeListener> fogIntensityChangeListeners = new ArrayList<>(4);
    private final List<PrecipitationChangeListener> precipitationChangeListeners = new ArrayList<PrecipitationChangeListener>(4);
    private final List<SkyColorChangeListener> skyColorChangeListeners = new ArrayList<>(4);
    private final List<SunColorChangeListener> sunColorChangeListeners = new ArrayList<>(4);
    private final List<SODChangeListener> sodChangeListeners = new ArrayList<>(4);
    private boolean isInitialised = false;

    /**
     * @param initialTimeOfDay example: 10.5 = 10:30 am
     */
    public WeatherProvider(float initialTimeOfDay, EnvColors initialEnvColors) {
        currentSOD = SECONDS_PER_HOUR * initialTimeOfDay;
        currentColorSchema = initialEnvColors;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        incSOD(delta);

        // send initial color, in case someone has been initialised wrongly
        sendInitialColorsIfNotDoneYet();

        // change the color scheme of the day at certain times
        changeColorsWithAccordingToDaytime();

        adjustPrecipitation();

//        if (currentSOD > (SECONDS_PER_HOUR * 10.5f) + 10f) {
//			triggerFogColorChange(EnvColors.DUSK.fog, 5f);
//			triggerAmbientColorChange(EnvColors.DUSK.ambient, 5f);
//			triggerSkyColorChange(EnvColors.DUSK.skyTop, EnvColors.DUSK.skyBottom, 5f);
//        }
    }

    private void adjustPrecipitation() {
        if (currentPrecipAction == null || !getActions().contains(currentPrecipAction, true)) {
            // time a for change of weather

            // list of possible next weathers
            ArrayList<Precipitation> nextWeathers = new ArrayList<Precipitation>();
            Collections.addAll(nextWeathers, Precipitation.values());

            // give CLEAR weather a higher chance to win :-)
            nextWeathers.add(Precipitation.CLEAR);
            nextWeathers.add(Precipitation.CLEAR);
            nextWeathers.add(Precipitation.CLEAR);

            // pick a random next weather from the list
            int nextWIndex = MathUtils.random(nextWeathers.size() - 1);
            Precipitation nextWeather = nextWeathers.get(nextWIndex);

            // pick again until the pick is valid
            boolean isValid = true;
            do {
                nextWIndex = MathUtils.random(nextWeathers.size() - 1);
                nextWeather = nextWeathers.get(nextWIndex);
                // don't let rain and snow follow each other
                // don't let anything else than clear sky follow fog (too much fog looks bad and precedes rains, snow anyway)
                if (currentPrecip == Precipitation.RAIN && nextWeather == Precipitation.SNOW)
                    isValid = false;
                else if (currentPrecip == Precipitation.SNOW && nextWeather == Precipitation.RAIN)
                    isValid = false;
                else if (currentPrecip == Precipitation.FOG && nextWeather != Precipitation.CLEAR)
                    isValid = false;
//				else if (currentPrecip != Precipitation.CLEAR && nextWeather == Precipitation.FOG)
//					isValid = false;
                else
                    isValid = true;

            } while (!isValid);

            // let all weather stay for 20 to 30 seconds
            final float d = MathUtils.random(20f, 30f);
            currentPrecipAction = MoreActions.floata(0, d, d);

            addAction(currentPrecipAction);
            currentPrecip = nextWeather;
            // System.out.println("next nextWeather" + nextWeather + " duration: " + d);

            // program the specific weather conditions
            switch (currentPrecip) {
                case CLEAR:
                    // clear sky has also a small amount of fog
                    sendFogIntensityChange(MathUtils.random(MIN_FOG, MAX_FOG_NO_PRECIPITATION), d * 0.2f);
                    break;
                case RAIN:
                    sendFogIntensityChange(MathUtils.random(MAX_FOG_NO_PRECIPITATION, MAX_FOG), d * 0.25f);
                    break;
                case SNOW:
                    sendFogIntensityChange(MathUtils.random(MAX_FOG_NO_PRECIPITATION, MAX_FOG), d * 0.2f);
                    break;
                case FOG:
                    sendFogIntensityChange(MathUtils.random(MAX_FOG_NO_PRECIPITATION, MAX_FOG), d);
                    break;

                default:
                    break;
            }

            // inform listeners
            sendPrecipChange(currentPrecip, d);
        } else {
            // there was no change and nothing is to be done
        }
//		System.out.println("comp "+ currentPrecipAction.isComplete());

    }

    /**
     * This method sets the colors according to the time.
     * But it only moves to the next color schema if the time has come.
     */
    private void changeColorsWithAccordingToDaytime() {
        switch (currentColorSchema) {
            case DAY:
                if (currentTOD() > 15.2f) {
                    currentColorSchema = currentColorSchema.next();
                    final float duration = 20f;
                    triggerColorSchemaChange(duration);
                }
                break;
            case DUSK:
                if (currentTOD() > 20.1f) {
                    currentColorSchema = currentColorSchema.next();
                    final float duration = 10f;
                    triggerColorSchemaChange(duration);
                }
                break;
            case NIGHT:
                if (currentTOD() > 3.9f && currentTOD() < 12f) {
                    currentColorSchema = currentColorSchema.next();
                    final float duration = 10f;
                    triggerColorSchemaChange(duration);
                }
                break;
            case DAWN:
                if (currentTOD() > 8.5f) {
                    currentColorSchema = currentColorSchema.next();
                    final float duration = 20f;
                    triggerColorSchemaChange(duration);
                }
                break;

            default:
                break;
        }

    }

    /**
     * Increment the Second Of Day ('SOD'), reset at midnight and inform all listeners.
     *
     * @param delta
     */
    private void incSOD(float delta) {
        currentSOD += delta;
        if (currentSOD >= SECONDS_PER_DAY)
            currentSOD = 0f; // start new day
        final float percentOfDayOver = currentSOD / SECONDS_PER_DAY;
        final float hourOfDay = currentSOD / SECONDS_PER_HOUR;
        for (SODChangeListener listener : sodChangeListeners) {
            listener.onSODChange(currentSOD, hourOfDay, delta, percentOfDayOver, currentColorSchema);
        }
//		System.out.println(String.format("SOD: %+10.4f", currentTOD() ));
    }

    /**
     * converts seconds of day to virtual time of day (0h - 24h)
     *
     * @return
     */
    public float currentTOD() {
        return currentSOD / SECONDS_PER_HOUR;
    }

    /**
     * for keyboard inputs
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // input key T
        if (Gdx.input.isKeyJustPressed(Keys.T)) {
            currentColorSchema = currentColorSchema.next();
            final float duration = 10f;
            triggerColorSchemaChange(duration);
        }
        if (Gdx.input.isKeyJustPressed(Keys.F)) {
            final float newval = Math.min(currentFog + FOG_STEPS, MAX_FOG);
            sendFogIntensityChange(newval, 1f);
            //System.out.println(String.format("fog: %+10.4f", currentFog));
        }
        if (Gdx.input.isKeyJustPressed(Keys.G)) {
            final float newval = Math.max(currentFog - FOG_STEPS, MIN_FOG);
            sendFogIntensityChange(newval, 1f);
            //System.out.println(String.format("fog: %+10.4f", currentFog));
        }
    }

    private void sendInitialColorsIfNotDoneYet() {
        if (!isInitialised) {
            triggerColorSchemaChange(1f);
            sendFogIntensityChange(MIN_FOG, 1f);
            isInitialised = true;
        }
    }

    private void sendFogIntensityChange(float targetIntensity, final float duration) {
        Gdx.app.log("WeatherProvider", "Ordering fog intensity " + targetIntensity + " in " + duration + " sconds");
        for (FogIntensityChangeListener listener : fogIntensityChangeListeners) {
            listener.onFogIntensityChanged(currentFog, targetIntensity, duration);
        }
        currentFog = targetIntensity;
    }

    private void sendPrecipChange(Precipitation targetPrecip, final float duration) {
        for (PrecipitationChangeListener listener : precipitationChangeListeners) {
            listener.onPrecipitationChanged(targetPrecip, duration);
        }
    }

    private void triggerColorSchemaChange(final float duration) {
        triggerAmbientColorChange(currentColorSchema.ambient, duration);
        triggerFogColorChange(currentColorSchema.fog, duration);
//		triggerSkyColorChange(currentColorSchema.skyTop, currentColorSchema.skyBottom, duration);
        triggerSkyColorChange(currentColorSchema.skyTop, currentColorSchema.fog, duration);
        triggerSunColorChange(currentColorSchema.sun, duration);
    }

    private void triggerAmbientColorChange(Color target, float duration) {
        for (AmbientColorChangeListener colorChangeListener : ambientColorChangeListeners) {
            colorChangeListener.onAmbientColorChangeTriggered(target, duration);
        }
    }

    private void triggerFogColorChange(Color target, float duration) {
        for (FogColorChangeListener colorChangeListener : fogColorChangeListeners) {
            colorChangeListener.onFogColorChangeTriggered(target, duration);
        }
    }

    private void triggerSkyColorChange(Color targetTop, Color targetBottom, float duration) {
        for (SkyColorChangeListener colorChangeListener : skyColorChangeListeners) {
            colorChangeListener.onSkyColorChangeTriggered(targetTop, targetBottom, duration);
        }
    }

    private void triggerSunColorChange(Color target, float duration) {
        for (SunColorChangeListener colorChangeListener : sunColorChangeListeners) {
            colorChangeListener.onSunColorChangeTriggered(target, duration);
        }
    }

    public void addAmbientColorChangeListener(AmbientColorChangeListener ambientColorChangeListener) {
        this.ambientColorChangeListeners.add(ambientColorChangeListener);
    }

    public void addFogColorChangeListener(FogColorChangeListener fogColorChangeListener) {
        this.fogColorChangeListeners.add(fogColorChangeListener);
    }

    public void addFogIntensityChangeListener(FogIntensityChangeListener fogIntensityChangeListener) {
        this.fogIntensityChangeListeners.add(fogIntensityChangeListener);
    }

    public void addPrecipChangeListener(PrecipitationChangeListener precipitationChangeListener) {
        this.precipitationChangeListeners.add(precipitationChangeListener);
    }

    public void addSkyColorChangeListener(SkyColorChangeListener skyColorChangeListener) {
        this.skyColorChangeListeners.add(skyColorChangeListener);
    }

    public void addSunColorChangeListener(SunColorChangeListener sunColorChangeListener) {
        this.sunColorChangeListeners.add(sunColorChangeListener);
    }

    public void addSODChangeListener(SODChangeListener sodChangeListener) {
        this.sodChangeListeners.add(sodChangeListener);
    }

    public EnvColors getCurrentColorSchema() {
        return currentColorSchema;
    }

    public float getCurrentFog() {
        return currentFog;
    }

    public Precipitation getCurrentPrecip() {
        return currentPrecip;
    }


}
