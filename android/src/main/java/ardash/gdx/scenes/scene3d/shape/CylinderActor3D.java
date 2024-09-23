package ardash.gdx.scenes.scene3d.shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ardash.gdx.scenes.scene3d.Actor3D;

/**
 * Created by boris on 24/05/2017.
 */

public class CylinderActor3D extends Actor3D {

    public CylinderActor3D(float radius, float height) {
        this(radius, height, Color.WHITE);
    }

    public CylinderActor3D(float radius, float height, Color color) {
        this(radius, height, color, new ModelBuilder());
    }

    public CylinderActor3D(float radius, float height, ModelBuilder modelBuilder) {
        this(radius, height, Color.WHITE, modelBuilder);
    }

    public CylinderActor3D(float radius, float height, Color color, ModelBuilder modelBuilder) {
        super(createModel(radius, height, color, null, modelBuilder));
    }

    public CylinderActor3D(float radius, float height, Texture texture, ModelBuilder modelBuilder) {
        super(createModel(radius, height, null, texture, modelBuilder));
    }

    public CylinderActor3D(float radius, float height, Color color, Texture texture, ModelBuilder modelBuilder) {
        super(createModel(radius, height, color, texture, modelBuilder));
    }

    private static Model createModel(float radius, float height, Color color, Texture texture, ModelBuilder modelBuilder) {
        Material material = new Material();
        if (color != null) material.set(ColorAttribute.createDiffuse(color));
        if (texture != null) material.set(TextureAttribute.createDiffuse(texture));

        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal + VertexAttributes.Usage.TextureCoordinates;

        return modelBuilder.createCylinder(radius, height, radius, Math.max(1, (int) (6 * (float) Math.cbrt(10 * radius))), material, usageCode);
    }
}
