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
 * Created by boris on 22/03/2017.
 */

public class CubeActor3D extends Actor3D {

    public CubeActor3D(float width, float height, float depth) {
        this(width, height, depth, Color.WHITE);
    }

    public CubeActor3D(float width, float height, float depth, Color color) {
        this(width, height, depth, color, new ModelBuilder());
    }

    public CubeActor3D(float width, float height, float depth, ModelBuilder modelBuilder) {
        this(width, height, depth, Color.WHITE, modelBuilder);
    }

    public CubeActor3D(float width, float height, float depth, Color color, ModelBuilder modelBuilder) {
        super(createModel(width, height, depth, color, null, modelBuilder));
    }

    public CubeActor3D(float width, float height, float depth, Texture texture, ModelBuilder modelBuilder) {
        super(createModel(width, height, depth, null, texture, modelBuilder));
    }

    public CubeActor3D(float width, float height, float depth, Color color, Texture texture, ModelBuilder modelBuilder) {
        super(createModel(width, height, depth, color, texture, modelBuilder));
    }

    private static Model createModel(float width, float height, float depth, Color color, Texture texture, ModelBuilder modelBuilder) {
        Material material = new Material();
        if (color != null) material.set(ColorAttribute.createDiffuse(color));
        if (texture != null) material.set(TextureAttribute.createDiffuse(texture));

        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked + VertexAttributes.Usage.Normal + VertexAttributes.Usage.TextureCoordinates;

        return modelBuilder.createBox(width, height, depth, material, usageCode);
    }
}
