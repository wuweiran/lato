package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.Farmhouse;
import ardash.lato.actors3.Spruce;
import ardash.lato.actors3.Stone;

/**
 * Almost straight initial terrain part.
 */
public class HomeHill extends Section {
    public HomeHill() {
        add(new Vector2(0, 0), new Vector2(10, 3), Interpolation.smooth);
        add(new Vector2(10, 3), new Vector2(25, 3), Interpolation.smooth);
        add(new Vector2(25, 3), new Vector2(40, 0), Interpolation.smooth);

        Farmhouse h1 = Pools.obtain(Farmhouse.class);
        h1.init();
        h1.init(-170f);
        h1.translate(10, 3.1f, -4);
        h1.setTag(Tag.BACK);

        Farmhouse h2 = Pools.obtain(Farmhouse.class);
        h2.init();
        h2.init(-70f);
        h2.translate(25, 3.1f, -20);
        h2.setTag(Tag.BACK);

        addSurroundingItem(h1);
        addSurroundingItem(h2);

        // add trees
        Spruce tree = new Spruce();
        tree.translate(12f, -2, -1);
        tree.setTag(Tag.BACK);
        surroundingItems.add(tree);
        Spruce tree2 = new Spruce();
        tree2.translate(20f, 0, 1);
        tree2.setTag(Tag.FRONT);
        surroundingItems.add(tree2);

        // add stone
        Stone stone = new Stone(2); // index 2 is the stone on the initial screen
        stone.setPosition(12.8f, 2.8f);
        surroundingItems.add(stone);
    }


}
