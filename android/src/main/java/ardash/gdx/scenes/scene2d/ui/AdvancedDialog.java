
package ardash.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import ardash.lato.A;
import ardash.lato.A.ARAsset;

public class AdvancedDialog extends Dialog {

    //	protected final Sprite EMPTY_TEX;
    protected final Image backgrPixel;

    public AdvancedDialog() {
        super("", new WindowStyle(
            A.FontAsset.F1_30_BOLD.font,
            Color.WHITE,
            null // TODO semi transparent pixel
        ));
        setModal(true);
        setMovable(false);
        setResizable(false);

//        EMPTY_TEX = Assets.SpriteAsset.BTN_SQ_EMPTY.get();
        backgrPixel = new Image(A.getTextureRegion(ARAsset.FOG_PIX));
        backgrPixel.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgrPixel.setColor(0, 0, 0, 0.5f);

        getButtonTable().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

    }

//	protected void button(LabelSpriteButton btnCancel) {
//		getButtonTable().add(btnCancel);
//		setObject(btnCancel, "");
//	}

    public Dialog text(String text, LabelStyle labelStyle, float wordWrapWidth) {
        //final Dialog ret = text(text, labelStyle);
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        label.setAlignment(Align.center, Align.center);
        label.setWidth(wordWrapWidth);
//		contentTable.add(label).width(wordWrapWidth).center();
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        backgrPixel.draw(batch, parentAlpha); // semi white background
        super.draw(batch, parentAlpha);
    }

}
