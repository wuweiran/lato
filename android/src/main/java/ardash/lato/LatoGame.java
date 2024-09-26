

package ardash.lato;

import android.content.Context;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import ardash.gdx.scenes.scene3d.pooling.PoolsManager;
import ardash.lato.screens.LoadingScreen;

public class LatoGame extends Game {
    public GameManager gm;

    @Override
    public void create() {
        PoolsManager.init();
        gm = new GameManager(this);
        setScreen(new LoadingScreen(gm));
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
        A.dispose();
    }
}
