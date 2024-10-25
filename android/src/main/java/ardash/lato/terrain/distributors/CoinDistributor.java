package ardash.lato.terrain.distributors;

import com.badlogic.gdx.math.MathUtils;

import java.util.SortedMap;

import ardash.lato.terrain.TerrainItemType;

public class CoinDistributor extends ColliderDistributor {

    private int currMaxX;

    public CoinDistributor() {
        super();
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        currMaxX = 100;// don't put anything in the first 100
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
        final int amount = MathUtils.random(1, 6);

        // check if there already coins in this range
        SortedMap<Integer, TerrainItemType> existingRange = getItemsInRange(start, start + amount);
        if (!existingRange.isEmpty()) {
            return 0;
        }

        for (int i = 0; i < amount; i++) {
            addItem(start + i);
        }
        return amount;
    }

    @Override
    protected void addItem(int i) {
//		final Coin coin = Pools.get(Coin.class).obtain();
//		coin.init();
//		coin.setX(i);
        getRangeMap().put(i, TerrainItemType.COIN);
    }

    @Override
    protected int getDesiredAmountPer1000m() {
        return 50;
    }
}
