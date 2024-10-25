package ardash.lato.actors;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


/**
 * Regulates the type and volume of Music. Uses the internal Actions and Interpolators to do a bit of simple fading (in and out).
 * Implemented as Singleton, so we can easily reach the instance from everywhere.
 *
 * @author Andreas Redmer
 */
public class MusicProvider extends Actor {
    private static MusicProvider INSTANCE;
    private Music currentMusic = null;

    private MusicProvider() {
        setX(0); // we use x for the volume
        setVisible(false);
        setTouchable(Touchable.disabled);
    }

    public static MusicProvider getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MusicProvider();
        return INSTANCE;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (currentMusic != null) {
            currentMusic.setVolume(getX());
        }
    }

    /**
     * fade out the current music and fade in the new one
     */
    public void fadeToMusic(final Music newMusic) {
        if (currentMusic == null) {
            currentMusic = newMusic;
            currentMusic.play();
            currentMusic.setVolume(1f);
            setX(1f);
            return;
        }
        final float fadeDuration = 1f;
        clearActions();
        addAction(Actions.sequence(
            Actions.moveTo(0f, -1f, fadeDuration), // fade out
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    // this is the procedure to switch over from one to another music
                    if (currentMusic != null)
                        currentMusic.pause();
                    newMusic.setVolume(0f);
                    newMusic.play();
                    currentMusic = newMusic;
                }
            }),
            Actions.moveTo(1f, -1f, fadeDuration) // fade in
        ));
    }
}
