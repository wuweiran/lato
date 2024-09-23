package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.Farmhouse;
import ardash.lato.actors3.Spruce;

/**
 * A slope downhill, followed by a short straight line.
 */
public class Hill extends Section {
    static final float MIN_ANGLE = 5f;
    static final float MAX_ANGLE = 15f;
    static final float MIN_LENGTH = 5f;
    static final float MAX_LENGTH = 15f;
    static final Vector2 currentRandomVector = new Vector2();

    public Hill() {
        makeNewRandomVector();
        add(new Vector2(0, 0), currentRandomVector, Interpolation.pow2);
        final Vector2 to = new Vector2(currentRandomVector);
        to.x *= 2;
        to.y = 0;
        add(to, Interpolation.pow2);

        // add trees
        Spruce tree = new Spruce();
        tree.translate(1f, -2, -1);
        tree.setTag(Tag.BACK);
        surroundingItems.add(tree);
        Spruce tree2 = new Spruce();
        tree2.translate(-5f, 0, 1);
        tree2.setTag(Tag.FRONT);
        surroundingItems.add(tree2);

//		Spruce tree3 = new Spruce();
//		tree3.translate(2, 0, -1.5f);
//		tree3.setTag(Tag.BACK);
//		surroundingItems.add(tree3);
//		Spruce tree4 = new Spruce();
//		tree4.translate(2.5f, 0, -0.1f);
//		tree4.setTag(Tag.BACK);
//		surroundingItems.add(tree4);

//      Farmhouse ma = new Farmhouse();
        Farmhouse ma = Pools.obtain(Farmhouse.class);
        ma.init();
        ma.translate(-1, 0, -30);
        ma.setTag(Tag.BACK);
        addSurroundingItem(ma);

    }

    static private void makeNewRandomVector() {
        currentRandomVector.set(1, 0).setLength(MathUtils.random(MIN_LENGTH, MAX_LENGTH));
        currentRandomVector.rotate(-MathUtils.random(MIN_ANGLE, MAX_ANGLE));
        // all lengths from x to x must be int
        currentRandomVector.x = MathUtils.ceil(currentRandomVector.x);
    }

}
