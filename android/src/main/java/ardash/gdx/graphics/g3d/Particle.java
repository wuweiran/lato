
package ardash.gdx.graphics.g3d;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;

public abstract class Particle extends Image3D {

    public static final ModelBuilder mb = new ModelBuilder();

    public Particle() {
        super(getTextureRegion(), mb);
        setName("Particle");
        setTag(Tag.CENTER);
    }

    private static TextureRegion getTextureRegion() {
        return A.getTextureRegion(ARAsset.FOG_PIX);
    }

    public abstract void init();

}
