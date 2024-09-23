package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import ardash.lato.terrain.TerrainSeg.TSType;

public class TerrainSegList extends ArrayList<TerrainSeg> {
    private final RangeMap<Float, TerrainSeg> rm = new RangeMap<Float, TerrainSeg>();

    /**
     * for method: add(Vector2 to, Interpolation i)
     */
    private Vector2 end = null;


    public void add(Vector2 from, Vector2 to, Interpolation i) {
        add(new TerrainSeg(from, to, i));
        end = to.cpy();
    }

    public void add(Vector2 from, Vector2 to, Interpolation i, TSType type) {
        add(new TerrainSeg(from, to, i, type));
        end = to.cpy();
    }

    public void add(Vector2 to, Interpolation i) {
        if (end == null)
            throw new RuntimeException("this function can only be used if there is already at least one vector");
        add(new TerrainSeg(end, to, i));
        end = to.cpy();
    }

    public float heightAt(float x) {
        if (x < first().x)
            throw new RuntimeException("x is below the range of this list: " + x + "is below " + first().x);
        if (x > last().x)
            throw new RuntimeException("x is above the range of this list: " + x);

        // get segment to be applied
        final TerrainSeg segement = rm.mappedVal(x);
        float fromX = segement.fromPoint.x;
        float toX = segement.toPoint.x;
        float range = toX - fromX;
        float fromY = segement.fromPoint.y;
        float toY = segement.toPoint.y;

        return segement.transistion.apply(fromY, toY, (x - fromX) / range);
    }

//	public Interpolation getTransistion(int index) {
//		return get(index).transistion;
//	}

    public Vector2 first() {
        if (isEmpty())
            throw new RuntimeException("list is empty");
        return get(0).fromPoint;
    }

    public Vector2 last() {
        if (isEmpty())
            throw new RuntimeException("list is empty");
        return get(size() - 1).toPoint;
    }

    public float firstX() {
        return first().x;
    }

    public float lastX() {
        return last().x;
    }

    public TerrainSeg removeFirst() {
        final TerrainSeg removedItem = super.remove(0);
//		removedItem.transistion
        // TODO free from object-Pool
        updateSearchIndex();
        return removedItem;
    }

    @Override
    public void add(int index, TerrainSeg element) {
        throw new RuntimeException("cannot add to middle of this list. append only");
    }

    @Override
    public TerrainSeg remove(int index) {
        throw new RuntimeException("cannot remove from middle of this list. removeFirst only");
    }

    @Override
    public boolean add(TerrainSeg e) {
        final boolean ret = super.add(e);
        updateSearchIndex();
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends TerrainSeg> c) {
        // add current last point as offset to all of them. All sections are modeled relatively.
        // This is the part of the code that attaches the new piece to the previous piece.
        final Vector2 offset = last();
        for (TerrainSeg ts : c) {
            ts.fromPoint.add(offset);
            ts.toPoint.add(offset);
        }
        return addAllNoOffset(c);
    }

    public boolean addAll(Collection<? extends TerrainSeg> c, final Vector2 offset) {
        for (TerrainSeg ts : c) {
            ts.fromPoint.add(offset);
            ts.toPoint.add(offset);
        }
        return addAllNoOffset(c);
    }

    /**
     * Adds new items without adding an offset. Can be used for the initial terrain part.
     *
     * @param c
     * @return
     */
    public boolean addAllNoOffset(Collection<? extends TerrainSeg> c) {
        final boolean ret = super.addAll(c);
        updateSearchIndex();
        return ret;
    }

    protected void updateSearchIndex() {
        rm.clear();
        for (TerrainSeg ts : this) {
            rm.put(ts.fromPoint.x, ts);
        }
        rm.put(this.last().x, null);

    }
}
