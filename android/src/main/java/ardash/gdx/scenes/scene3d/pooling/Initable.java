package ardash.gdx.scenes.scene3d.pooling;

import com.badlogic.gdx.utils.Pool.Poolable;

public interface Initable extends Poolable {

    void init();

    boolean isInitialized();

    default void verify() {
        if (!isInitialized()) {
            System.err.println("Item " + this.getClass().getSimpleName() + " has act()-ed but was not initialised. Call init after unpooling it.");
//			throw new IllegalStateException("Item "+this.getClass().getSimpleName()+" has act()-ed but was not initialised. Call init after unpooling it.");
        }
    }
}
