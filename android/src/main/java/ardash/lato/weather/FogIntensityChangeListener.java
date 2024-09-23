package ardash.lato.weather;

public interface FogIntensityChangeListener {

    /**
     * Callback for the change of fog intensity.
     *
     * @param newIntensity a value from 0.0f (no fog) to 1.0f (maximum fog)
     */
    void onFogIntensityChanged(float currentFog, float newIntensity, final float duration);

}
