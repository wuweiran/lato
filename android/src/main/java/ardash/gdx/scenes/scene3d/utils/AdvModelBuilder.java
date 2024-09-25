package ardash.gdx.scenes.scene3d.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class AdvModelBuilder extends ModelBuilder {

    private final VertexInfo vertTmp1 = new VertexInfo();
    private final VertexInfo vertTmp2 = new VertexInfo();
    private final VertexInfo vertTmp3 = new VertexInfo();
    private final VertexInfo vertTmp4 = new VertexInfo();

    public Model createTria(Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3,
                            final Material material,
                            final long attributes) {
        begin();
//			part("tria", GL20.GL_TRIANGLES, attributes, material).triangle(p1, c1, p2, c2, p3, c3);
//			part("tria", GL20.GL_TRIANGLES, attributes, material).triangle(p1, c1, p2, c2, p3, c3);
        vertTmp1.setPos(p1);
        vertTmp2.setPos(p2);
        vertTmp3.setPos(p3);
        vertTmp1.setNor(0, 0, 1);
        vertTmp2.setNor(0, 0, 1);
        vertTmp3.setNor(0, 0, 1);
        vertTmp1.setCol(c1);
        vertTmp2.setCol(c2);
        vertTmp3.setCol(c3);
        part("tria", GL20.GL_TRIANGLES, attributes, material).triangle(vertTmp1, vertTmp2, vertTmp3);
//			part("tria", GL20.GL_TRIANGLES, attributes, material).triangle(vertTmp1, vertTmp2, vertTmp3);
        return end();
    }
}
