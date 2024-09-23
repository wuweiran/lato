package ardash.lato.weather;

import ardash.lato.weather.WeatherProvider.Precipitation;

public interface PrecipitationChangeListener {

    /**
     * Listener callback to be notified when the weather changes
     *
     * @param targetPrecipitation RAIN, SNOW, ...
     * @param seconds             the duration it lasts for
     */
    void onPrecipitationChanged(Precipitation targetPrecipitation, float seconds);

}
