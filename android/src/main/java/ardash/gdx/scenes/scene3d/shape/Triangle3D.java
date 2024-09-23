package ardash.gdx.scenes.scene3d.shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

public class Triangle3D extends Actor3D {

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
    public Triangle3D(Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3, ModelBuilder modelBuilder) {
        super(createModel(p1, c1, p2, c2, p3, c3, modelBuilder));
    }

    private static Model createModel(Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3, ModelBuilder modelBuilder) {
        Material material = new Material();
//        if (c1 != null) material.set( ColorAttribute.createDiffuse(c1) );
//        if (texture != null) material.set( TextureAttribute.createDiffuse(texture) );

//        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal + VertexAttributes.Usage.TextureCoordinates;

        // don't use normals, so these triangles can't be affected by light
        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal;
        material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 1.0f));

//        Gdx.gl20.glPolygonOffset(factor, units);
        AdvModelBuilder mb = new AdvModelBuilder(); // TODO mb = input parameter
        return mb.createTria(p1, c1, p2, c2, p3, c3, material, usageCode);
//        return modelBuilder.createRect(0, 0, 0, width, 0, 0, width, height, 0, 0, height, 0, 0, 0, 1, material, usageCode) ;
    }

}
