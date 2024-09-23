
package ardash.lato.terrain.distributors;

import java.util.SortedMap;

import com.badlogic.gdx.math.MathUtils;

import ardash.lato.terrain.CollidingTerrainItem;
import ardash.lato.terrain.TerrainItemType;

public class StoneDistributor extends ColliderDistributor {

    private int currMaxX;

    public StoneDistributor() {
        super();
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        currMaxX = 500;// don't put anything in the first 100
    }

    public int getCurrMaxX() {
        return currMaxX;
    }

    public void setCurrMaxX(int currMaxX) {
        this.currMaxX = currMaxX;
    }

    @Override
    protected int addAFewItems(int from, int to) {
        // get a random starting point in this range
        final int start = MathUtils.random(from, to);

        //get a random amount of coins
        final int amount = (int) MathUtils.randomTriangular(1, 4, 1); // max 3 stones in a row, but 1 is most likely

        // check if there already coins in this range
        SortedMap<Integer, TerrainItemType> existingRange = getItemsInRange(start, start + amount);
        if (!existingRange.isEmpty()) {
            return 0;
        }

        for (int i = 0; i < amount * 3; i++) {
            addItem(start + i);
            i++;
            addDummyItem(start + i);
            i++;
            addDummyItem(start + i);
        }
        return amount;
    }

    @Override
    protected void addItem(int i) {
//		final Stone s = new Stone();
//		s.setX(i);
        getRangeMap().put(i, TerrainItemType.STONE);
    }

    @Override
    protected int getDesiredAmountPer1000m() {
        return 5;
    }

}
