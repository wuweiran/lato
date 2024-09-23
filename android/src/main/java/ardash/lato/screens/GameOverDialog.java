package ardash.lato.screens;

import android.content.Context;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import ardash.gdx.scenes.scene2d.ui.AdvancedDialog;
import ardash.lato.A;
import ardash.lato.actors.StageAccessor;

public class GameOverDialog extends AdvancedDialog implements StageAccessor {

    public GameOverDialog(String causeOfDeath, int distance) {
        super();
        setTouchable(Touchable.disabled);
        Label lblGameOver = new Label("Game Over", A.LabelStyleAsset.DISTANCE_LABEL.style);
        text(lblGameOver);
        getContentTable().row();
        text(new Label(causeOfDeath, A.LabelStyleAsset.SMALL_TEXT.style));
        getContentTable().row();
        text(new Label("You travelled "+distance+" meters", A.LabelStyleAsset.SMALL_TEXT.style));
        getContentTable().row();
        text(new Label("You collected "+getGameManager().getCoinsPickedUpThisRound()+" coins", A.LabelStyleAsset.SMALL_TEXT.style));
        getContentTable().row();
        text(new Label("Touch the screen to restart", A.LabelStyleAsset.SMALL_TEXT.style));

//		getGameScreen().performer.getTimeInState()
    }

//	@Override
//	public Actor hit(float x, float y, boolean touchable) {
//		// TODO Auto-generated method stub
//		return super.hit(x, y, touchable);
//	}

}
