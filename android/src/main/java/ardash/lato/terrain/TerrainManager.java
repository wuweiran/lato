
package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import ardash.lato.actors3.Coin;
import ardash.lato.actors3.Stone;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.distributors.CoinDistributor;
import ardash.lato.terrain.distributors.StoneDistributor;
import ardash.lato.terrain.distributors.TerrainItemDistributor;

/**
 * provides new randomised terrain following different generation-strategies.
 * Supplies, shape of the ground, trees, houses, villages, forests, grind lines, ramps, stones etc.
 *
 * @author z
 */
public class TerrainManager {

    public interface TerrainListener {
        void onNewSectionCreated(Section s);
    }

    /**
     * List of all sections of the current terrain. Will be truncated, when a new round starts.
     * Appends new items but does not delete first items in the beginning, so we an look back of what we have passed in the current round.
     */
    List<Section> sections = new ArrayList<Section>();

    List<TerrainListener> listeners = new ArrayList<TerrainListener>();

    private TerrainItemDistributor cd = new CoinDistributor();
    private TerrainItemDistributor sd = new StoneDistributor();

    public TerrainManager() {
        reset();
    }

    // TODO dont reset, make a new one
    private void reset() {
        for (Section section : sections) {
            for (TerrainItem ti : section.surroundingItems) {
                Pools.free(ti);
            }
        }
        sections.clear();
        listeners.clear();
        cd.reset();
        sd.reset();
    }

    public Section getLastSection() {
        if (sections.isEmpty())
            throw new RuntimeException("there are no terrain sections yet");
        return sections.get(sections.size() - 1);
    }

    public void createNewSection() {
        System.out.println("NEW SEGMENT");
        System.gc();
        Section s;
        if (sections.isEmpty()) {
            s = new HomeHill();
            sections.add(s);
        } else {
            s = new Downer();
            if (MathUtils.randomBoolean(0.1f))
                s = new SteepDowner();
            if (MathUtils.randomBoolean(0.1f))
                s = new Hill();
            if (MathUtils.randomBoolean(0.2f))
                s = new Canyon();
            if (MathUtils.randomBoolean(0.1f))
                s = new Village();
            final Vector2 offset = this.getLastSection().last();

            // the type and size of the new sections is now final

            final float offsetX = offset.x;
            addCoinsAndStones(s, offsetX);
            s.addOffsetToSurroundings(offset);
            s.addOffsetToSegList(offset);
            sections.add(s);
        }

        for (TerrainListener listener : listeners) {
            listener.onNewSectionCreated(s);
            s.validate();
        }
    }

    private void addCoinsAndStones(Section s, final float offsetX) {
        Set<Integer> itemsToDelete = new HashSet<>();
        // put some coins on the new section, if there are coins planned for it
        {
            SortedMap<Integer, TerrainItemType> plannedCoinsInRange = cd.getItemsInRange((int) (s.firstX() + offsetX), MathUtils.ceil(s.lastX() + offsetX));
            for (int plannedCoinX : plannedCoinsInRange.keySet()) {
                if (plannedCoinsInRange.get(plannedCoinX) != TerrainItemType.COIN) {
                    continue;
                }
                itemsToDelete.add(plannedCoinX);
                final Coin cti = Pools.get(Coin.class).obtain();
                cti.init();
                // the CTI are being created with a wider view, so they already have the absolute X value correct: now move them back
                cti.setPosition(plannedCoinX, 0.7f);
                cti.moveBy(-offsetX, 0);
                cti.moveBy(0, s.heightAt(cti.getX()));
                s.surroundingItems.add(cti);
            }
        }

        // put some stones on the new section, if there are stones planned for it
        {
            SortedMap<Integer, TerrainItemType> plannedStonesInRange = sd.getItemsInRange((int) (s.firstX() + offsetX), MathUtils.ceil(s.lastX() + offsetX));
            for (int plannedStoneX : plannedStonesInRange.keySet()) {
                if (plannedStonesInRange.get(plannedStoneX) != TerrainItemType.STONE) {
                    continue;
                }
                itemsToDelete.add(plannedStoneX);
                final Stone cti = Pools.get(Stone.class).obtain();
//					cti.init(); // TODO does stone need no init ?
                // the CTI are being created with a wider view, so they already have the absolute X value correct: now move them back
                cti.setPosition(plannedStoneX, -0.5f);
                cti.moveBy(-offsetX, 0);
                cti.moveBy(0, s.heightAt(cti.getX()));
                s.surroundingItems.add(cti);
                addCoinsAboveStones(s, cti);
            }

            // delete is fromt the distributors rangemap
            for (Integer integer : itemsToDelete) {
                plannedStonesInRange.remove(integer);
            }
        }
    }

    private void addCoinsAboveStones(Section s, final Stone stone) {
        // only in 30% of the stones,there shall be coins floating above it
        final boolean putCoinsAboveStone = MathUtils.randomBoolean(0.30f);
        if (!putCoinsAboveStone)
            return;

        final int randomPatternIndex = MathUtils.random(1, 3);
        final float heightOverStone = MathUtils.random(0.3f, 4.0f) + 2.2f;
        switch (randomPatternIndex) {
            case 1:
                // 1 coin over stone
            {
                final Coin coin = Pools.get(Coin.class).obtain();
                coin.init();
                coin.setPosition(stone.getX(), stone.getY());
                coin.moveBy(1f, heightOverStone);
                s.surroundingItems.add(coin);
            }
            break;

            case 2:
                // 3 coins over stone
                for (int i = 0; i < 3; i++) {
                    final Coin coin = Pools.get(Coin.class).obtain();
                    coin.init();
                    coin.setPosition(stone.getX(), stone.getY());
                    coin.moveBy(i, heightOverStone);
                    s.surroundingItems.add(coin);
                }
                break;

            case 3:
                // 6 coins over stone
                for (int i = 0; i < 3; i++) {
                    final Coin coin = Pools.get(Coin.class).obtain();
                    coin.init();
                    coin.setPosition(stone.getX(), stone.getY());
                    coin.moveBy(i, heightOverStone);
                    s.surroundingItems.add(coin);
                }
                for (float i = 0.5f; i < 2; i += 0.5f) {
                    final Coin coin = Pools.get(Coin.class).obtain();
                    coin.init();
                    coin.setPosition(stone.getX(), stone.getY());
                    coin.moveBy(i, (i == 1.0f ? 1f : 0.5f) + heightOverStone);
                    s.surroundingItems.add(coin);
                }
                break;
            default:
                break;
        }
    }

    public void addListener(TerrainListener listener) {
        this.listeners.add(listener);
    }

    public List<Section> getSections() {
        return sections;
    }

}
