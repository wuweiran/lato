package ardash.gdx.scenes.scene3d.shape;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.utils.AdvModelBuilder;

/**
 * Created by Andreas Redmer on 12/04/2020.
 */

public class Circle3D extends Actor3D {

    /**
     * Vertices must be counter-clockwise
     *
     * @param p1
     * @param c1
     * @param p2
     * @param c2
     * @param p3
     * @param c3
     * @param modelBuilder
     */
    public Circle3D(float radius, int divisions, Vector3 center, ModelBuilder modelBuilder) {
        super(createModel(radius, divisions, center, modelBuilder));
    }

    private static Model createModel(float radius, int divisions, Vector3 center, ModelBuilder modelBuilder) {
        Material material = new Material();
//        if (c1 != null) material.set( ColorAttribute.createDiffuse(c1) );
//        if (texture != null) material.set( TextureAttribute.createDiffuse(texture) );

//        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal + VertexAttributes.Usage.TextureCoordinates;

        // don't use normals, so these triangles can't be affected by light
        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal;
        material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 1.0f));

//        Gdx.gl20.glPolygonOffset(factor, units);
        AdvModelBuilder mb = new AdvModelBuilder();
        final Model ret = mb.createCirc(radius, divisions, center, null, material, usageCode);
        for (VertexAttribute attribute : ret.meshes.get(0).getVertexAttributes()) {
            final VertexAttribute colorPacked = attribute.ColorPacked();
            int i = 0;
        }
        return ret;
//        return modelBuilder.createRect(0, 0, 0, width, 0, 0, width, height, 0, 0, height, 0, 0, 0, 1, material, usageCode) ;
    }

}
