package ardash.lato;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.PerformanceCounters;

import ardash.lato.A.SoundAsset;
import ardash.lato.screens.GameScreen;
import ardash.lato.terrain.TerrainManager;
import ardash.lato.utils.SoundPlayer;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.SODChangeListener;

public class GameManager implements SODChangeListener {

    public static final boolean DEBUG_VIEW = false;
    public static final boolean DEBUG_GUI = false;
    public static final boolean DEBUG_RUNTIME_VALIDATION = false;
    public static final boolean DEBUG_WEATHER_FASTMODE = false;
    //	public static final boolean DEBUG_WEATHER_FASTMODE = true;
    public static final boolean DEBUG_ZOOM_OUT_TO_MAX_SPEED = false;
    public static final boolean DEBUG_PRINT_PERFORMANCE_STATS = false;
    public static final boolean DEBUG_PRINT_POOL_STATS = false;

    public final LatoGame game;
    public TerrainManager tm;
    public PerformanceCounters performanceCounters = new PerformanceCounters();

    /**
     * Indicates if forward movement is going on. User must tap initially to start and movement will end after crash.
     */
    private boolean started;
    private float lastHourOfDay = -1;
    private EnvColors lastKnownColorScheme = EnvColors.DAY;
    private int coinsPickedUpThisRound;

    public GameManager(LatoGame game) {
        this.game = game;
        this.tm = new TerrainManager();
        reset();
    }

    public void reset() {
        this.tm = new TerrainManager();
        started = false;
        coinsPickedUpThisRound = 0;
    }

    public Screen getScreen() {
        return game.getScreen();
    }

    public GameScreen getGameScreen() {
        return (GameScreen) game.getScreen();
    }

    private boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver, EnvColors currentColorSchema) {
        this.lastHourOfDay = hourOfDay;
        this.lastKnownColorScheme = currentColorSchema;
    }

    public float getLastHourOfDay() {
        return lastHourOfDay;
    }

    public void pickUpCoin() {
        SoundPlayer.playSound(A.getSound(SoundAsset.COINDROP));
        coinsPickedUpThisRound++;
    }

    public int getCoinsPickedUpThisRound() {
        return coinsPickedUpThisRound;
    }

    public EnvColors getLastKnownColorScheme() {
        return lastKnownColorScheme;
    }

}
