package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class HorizontalLine extends Actor implements Disposable {

    private ShapeRenderer sr;

    public HorizontalLine(float width, float weight, Color color) {
        setSize(width, weight);
        setColor(color);

        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.end();

        Vector2 coords = new Vector2(getX(), getY());

        Color color = new Color(getColor());
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.rectLine(coords.x, coords.y, coords.x + getWidth(), coords.y, getHeight());
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Gdx.gl.glLineWidth(1f);
        sr.setColor(Color.WHITE);

        batch.begin();
    }

    @Override
    public void dispose() {
        sr.dispose();
    }
}
