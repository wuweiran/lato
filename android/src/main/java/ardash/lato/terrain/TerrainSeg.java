package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class TerrainSeg {
    public Vector2 fromPoint;
    public Vector2 toPoint;
    public Interpolation transistion;
    public TSType type;
    public TerrainSeg(Vector2 from, Vector2 to, Interpolation transistion) {
        this(from, to, transistion, TSType.GROUND);
    }

    public TerrainSeg(Vector2 from, Vector2 to, Interpolation transistion, TSType type) {
        this.fromPoint = from.cpy();
        this.toPoint = to.cpy();
        this.transistion = transistion;
        this.type = type;
    }

    public enum TSType {
        GROUND, ABYSS, ROPE
    }
}
