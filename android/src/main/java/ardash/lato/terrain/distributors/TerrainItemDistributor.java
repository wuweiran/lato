package ardash.lato.terrain.distributors;

import java.util.SortedMap;
import java.util.TreeMap;

import ardash.lato.terrain.TerrainItemType;

public abstract class TerrainItemDistributor {

    static final int AVG_RANGE_SIZE = 500;

    public TerrainItemDistributor() {
        super();
    }

    protected abstract void addItem(int i);

    protected abstract int addAFewItems(int from, int to);

    public void reset() {
//		for (TerrainItem ti : getRangeMap().values()) {
//			Pools.free(ti);
//		}
        getRangeMap().clear();
    }

    public abstract int getCurrMaxX();

    public abstract void setCurrMaxX(int newCurrMaxX);

    protected abstract int getDesiredAmountPer1000m();

    /**
     * Some items cannot oerlap, like stones and coins. So stones an coins share the same range map.
     * The range map is static. For stones and coines it is the colliderRagemap.
     *
     * @return The Rage Map for this Type.
     */
    protected abstract TreeMap<Integer, TerrainItemType> getRangeMap(); // TODO here. Float is not godd, because wwhen fetching items in range, we can distingush betweem coin and stone. letst use an Enum for the type of item. however, oit must no be the otem itself

    public SortedMap<Integer, TerrainItemType> getItemsInRange(int from, int to) {
        if (to > getCurrMaxX()) {
            generateNewRange();
        }
        return getRangeMap().subMap(from, to);
    }

//	public void removeItemsBefore(int to) {
//		SortedMap<Integer, CollidingTerrainItem> subMap = getItemsInRange(Integer.MIN_VALUE, to);
//		for (Integer i : subMap.keySet()) {
//			final CollidingTerrainItem removedValue = subMap.remove(i);
//			// TODO ERROR here: this still hold refenrecs of coins that were on the stage and have been picked up, an freed already, now we free them again, while they are in use, prolly no godd idea to make the rferences in the distributor, solution, whne items to to stage, remove them from her rangemap
//			//Pools.free(removedValue);
//		}
//	}

    private void generateNewRange() {
        final int from = getCurrMaxX();
        final int to = from + AVG_RANGE_SIZE;
        final int rangeSize = to - from;
        setCurrMaxX(to);

        final int desiredAmountPer1000m = getDesiredAmountPer1000m();
        final int desiredAmountForThisRange = desiredAmountPer1000m / Math.min(1, (1000 / rangeSize));

        int addedItems = 0;

        for (int i = 0; i < desiredAmountForThisRange; i++) {
            final int itemsAddedInThisCycle = addAFewItems(from, to);
            addedItems += itemsAddedInThisCycle;

            // stop when we have enough items
            if (addedItems >= desiredAmountForThisRange) {
                break;
            }
        }
    }

    /**
     * Adds a dummy item to fill the place at position i, so no other object can be palced there. Anyway the dummy won't be rendered or colliding.
     * It is only sitting there to save the spot. Stones for example must have a minimum distance of 1. We can put dummy items next to them, so no other stones will be put there.
     *
     * @param i the x index
     */
    protected void addDummyItem(int i) {
//		final DummyTerrainItem di = new DummyTerrainItem();
        getRangeMap().put(i, TerrainItemType.DUMMY);
    }


}
