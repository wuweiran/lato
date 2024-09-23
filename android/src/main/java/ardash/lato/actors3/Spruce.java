package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;

import ardash.gdx.scenes.scene3d.shape.Image3D;

public class Spruce extends Image3D implements TerrainItem {

    public Spruce() {
        super(5, 5, new Color(48 / 255f, 105 / 255f, 105 / 255f, 1f)); //back color
//		super(5,5,new Color(32 / 255f, 69 / 255f, 69 / 255f, 1f) ); //front color
        setName("Spruce");
        rotateBy(45f);
        scale(0.01f, 5, 1);
        moveBy(0, -35f);
//		scale(0.01f);
//		setScale(1,1,1);
    }

}
