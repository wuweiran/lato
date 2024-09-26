package ardash.gdx.scenes.scene3d.pooling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pools;

import ardash.lato.actors.particles.SnowParticle;
import ardash.lato.actors3.Coin;
import ardash.lato.actors3.Farmhouse;
import ardash.lato.actors3.Stone;

public class PoolsManager {
    public static void init() {
        Pools.get(Farmhouse.class, 100);
        Pools.get(SnowParticle.class, 200);
        Pools.get(Coin.class, 100);
        Pools.get(Stone.class, 20);
        Gdx.app.log("PoolsManager", "poolsinit");
    }

    public static void printStatusOutput() {
        Gdx.app.log("PoolsManager", "Farmhouse: " + Pools.get(Farmhouse.class).getFree() + " peak: " + Pools.get(Farmhouse.class).peak);
        Gdx.app.log("PoolsManager", "SnowParticle: " + Pools.get(SnowParticle.class).getFree() + " peak: " + Pools.get(SnowParticle.class).peak);
        Gdx.app.log("PoolsManager", "Coin: " + Pools.get(Coin.class).getFree() + " peak: " + Pools.get(Coin.class).peak);
        Gdx.app.log("PoolsManager", "Stone: " + Pools.get(Stone.class).getFree() + " peak: " + Pools.get(Stone.class).peak);
    }
}
