package ardash.lato.weather;

/**
 * SOD = Second of Day.
 * Listen to this to be continuously informed about the change of the current time of the virtual day.
 *
 * @author z
 */
public interface SODChangeListener {
    void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver, EnvColors currentColorSchema);
}
