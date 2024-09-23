
package ardash.lato.terrain;

import ardash.lato.actors3.TerrainItem;

/**
 * Indicates a Terrain Item that can collide with the Performer (like stones, ramps, coins).
 */
public interface CollidingTerrainItem extends TerrainItem {

    default void act(float delta) {
        // by doing collision detection here we reduce the work and the code needed in Performer (ie. if there is no Stone, nothing need to be done))
        detectCollision();
    }

    void detectCollision();

    void onCollision();

}
