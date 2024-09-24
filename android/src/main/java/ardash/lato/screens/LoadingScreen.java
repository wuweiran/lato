package ardash.lato.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ardash.lato.A;
import ardash.lato.GameManager;
import ardash.lato.LatoGame;
import ardash.lato.weather.EnvColors;

public class LoadingScreen implements Screen {
    GameManager gm;
    private Stage stage;
    private LatoGame game;
    Label title;

    public LoadingScreen(GameManager gm) {
        this.gm = gm;
        this.game = gm.game;

        stage = new Stage(new ScreenViewport());

        title = new Label(A.getI18NBundle().get("loading"), A.LabelStyleAsset.DISTANCE_LABEL.style);
        title.setAlignment(Align.center);
        title.setY((float) (Gdx.graphics.getHeight() * 2) / 3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
    }

    @Override
    public void show() {
        A.enqueueAll();
    }

    @Override
    public void render(float delta) {
        final boolean done = A.loadAsync();
        final float p = A.getProgress();
        if (done) {
            game.setScreen(new GameScreen(gm));
        }
        Gdx.app.log("LoadingScreen", "still loading ...");
        // draw something nice to look at
        Gdx.gl.glClearColor(p * EnvColors.DAY.fog.r, p * EnvColors.DAY.fog.g, p * EnvColors.DAY.fog.b, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        title.setText(A.getI18NBundle().format("loadingPercentage", A.getPercentLoaded()));
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
