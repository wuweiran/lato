package ardash.lato.screens;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.I18NBundle;

import ardash.gdx.scenes.scene2d.ui.AdvancedDialog;
import ardash.lato.A;
import ardash.lato.actors.Performer;
import ardash.lato.actors.StageAccessor;

public class GameOverDialog extends AdvancedDialog implements StageAccessor {

    public GameOverDialog(Performer.Demise demise, int distance) {
        super();
        I18NBundle i18NBundle = A.getI18NBundle();
        setTouchable(Touchable.disabled);
        Label lblGameOver = new Label(A.getI18NBundle().get("gameOver"), A.LabelStyleAsset.DISTANCE_LABEL.style);
        text(lblGameOver);
        getContentTable().row();
        String causeOfDeath = switch (demise) {
            case HIT_STONE -> i18NBundle.get("hitStone");
            case LAND_ON_ASS -> i18NBundle.get("landOnAss");
            case LAND_ON_NOSE -> i18NBundle.get("landOnNose");
            case LAND_ON_STONE -> i18NBundle.get("landOnStone");
            case DROP_IN_CANYON -> i18NBundle.get("dropInCanyon");
            case NONE -> i18NBundle.get("unknownDeath");
        };
        text(new Label(causeOfDeath, A.LabelStyleAsset.SMALL_TEXT.style));
        getContentTable().row();
        text(new Label(A.getI18NBundle().format("travelledDistance", distance), A.LabelStyleAsset.SMALL_TEXT.style));
        getContentTable().row();
        text(new Label(A.getI18NBundle().format("collectedCoins", getGameManager().getCoinsPickedUpThisRound()), A.LabelStyleAsset.SMALL_TEXT.style));
        getContentTable().row();
        text(new Label(A.getI18NBundle().get("toRestart"), A.LabelStyleAsset.SMALL_TEXT.style));

//		getGameScreen().performer.getTimeInState()
    }
}
