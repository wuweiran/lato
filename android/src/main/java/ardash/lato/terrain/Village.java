package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import java.util.Arrays;
import java.util.List;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.Farmhouse;
import ardash.lato.actors3.Spruce;

/**
 * Almost straight initial terrain part.
 */
public class Village extends Section {
    public Village() {
        add(new Vector2(0, 0), new Vector2(10, -3), Interpolation.smooth);
        add(new Vector2(10, -3), new Vector2(25, -3.1f), Interpolation.smooth);
        add(new Vector2(25, -3.1f), new Vector2(40, -3.9f), Interpolation.smooth);

        // place some houses in background (x 1, 15, 28)
        for (int hx : Arrays.asList(1, 15, 28)) {
            if (MathUtils.randomBoolean(0.05f))
                continue; // with a small chance this house won't be drawn, to increase randomness
            Farmhouse h1 = Pools.obtain(Farmhouse.class);
            final float rotationAngle = MathUtils.random(-360f, 360f);
            final float heightAboveGround = MathUtils.random(-2f, 0f);
            final float randomZ = MathUtils.random(-4f, -20f);
            h1.init(rotationAngle);
            h1.translate(hx, heightAboveGround, randomZ);
            h1.setTag(Tag.BACK);
            addSurroundingItem(h1);
        }

        // place some houses in foreground (x 7, 21) (note: not too many, since they hid the stones and make the player crash)
        for (int hx : Arrays.asList(7, 21)) {
            if (MathUtils.randomBoolean(0.17f))
                continue; // with a small chance this house won't be drawn, to increase randomness
            Farmhouse h1 = Pools.obtain(Farmhouse.class);
            final float rotationAngle = MathUtils.random(-360f, 360f);
            final float heightAboveGround = MathUtils.random(-2.5f, -5f);
            final float randomZ = MathUtils.random(4f, 20f);
            h1.init(rotationAngle);
            h1.translate(hx, heightAboveGround, randomZ);
            h1.setTag(Tag.FRONT);
            addSurroundingItem(h1);
        }

        // add trees in background
        for (int hx : Arrays.asList(7, 21, 32)) {
            Spruce tree = new Spruce();
            final float heightAboveGround = MathUtils.random(-7f, -2f);
            final float randomZ = MathUtils.random(-2f, -1f);
            tree.translate(hx, heightAboveGround, randomZ);
            tree.setTag(Tag.BACK);
            surroundingItems.add(tree);
        }

        // add trees in foreground
        for (int hx : List.of(14)) {
            Spruce tree = new Spruce();
            final float heightAboveGround = MathUtils.random(-7f, -2f);
            final float randomZ = MathUtils.random(0.8f, 1.7f); // not too high, they cover too many stones
            tree.translate(hx, heightAboveGround, randomZ);
            tree.setTag(Tag.FRONT);
            surroundingItems.add(tree);
        }
//		Spruce tree2 = new Spruce();
//		tree2.translate(20f, 0, 1);
//		tree2.setTag(Tag.FRONT);
//		surroundingItems.add(tree2);

        // add stone
//		Stone stone = new Stone(2); // index 2 is the stone on the initial screen
//		stone.setPosition(12.8f, 2.8f);
//		surroundingItems.add(stone);
    }


}
