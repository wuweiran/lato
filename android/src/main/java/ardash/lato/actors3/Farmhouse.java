package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.pooling.PoolableActor3D;
import ardash.lato.A;
import ardash.lato.A.ModelAsset;

public class Farmhouse extends PoolableActor3D implements TerrainItem {

    //	private Farmhouse(float rotation) {
//		super(getModel());
//		setName("farmhouse");
//		setScale(2.17f); // farmhouse
//        translate(0, -0.6f, 0);
//        setColor(Color.WHITE);
//	}
    public Farmhouse() {
        super(getModel());
        setName("farmhouse");
        setScale(2.17f); // farmhouse
        translate(0, -0.6f, 0);
        setColor(Color.LIGHT_GRAY);
//        model.materials.get(0).set(ColorAttribute.createSpecular(Color.WHITE));
        model.materials.get(0).remove(ColorAttribute.Specular);
        model.materials.get(0).remove(ColorAttribute.Ambient);
        model.materials.get(0).remove(ColorAttribute.Emissive);
//        model.materials.get(0).remove(ColorAttribute.Diffuse);
    }

    @Override
    public void draw(ModelBatch modelBatch, Environment environment) {
        // TODO Auto-generated method stub
        super.draw(modelBatch, getStage().dirLightenvironment);
    }

    private static Model getModel() {
        return A.getModel(ModelAsset.FARMHOUSE);
    }

    @Override
    public void init() {
        super.init();
        setPitch(MathUtils.random(360f));
        setPosition(0, 0, 0);
        translate(0, -0.6f, 0);
    }

    public void init(float rotation) {
        this.init();
        setPitch(rotation);
    }

//	@Override
//		public boolean remove() {
//		if (hasParent())
//			throw new RuntimeException("tmp");
//		return super.remove();
//		}
}
