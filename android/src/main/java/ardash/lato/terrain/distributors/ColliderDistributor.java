
package ardash.lato.terrain.distributors;

import java.util.TreeMap;

import ardash.lato.terrain.TerrainItemType;

public abstract class ColliderDistributor extends TerrainItemDistributor {

    private static final TreeMap<Integer, TerrainItemType> COLLIDER_RANGE_MAP = new TreeMap<>();

    public ColliderDistributor() {
        super();
    }

    @Override
    protected TreeMap<Integer, TerrainItemType> getRangeMap() {
        return COLLIDER_RANGE_MAP;
    }

}
