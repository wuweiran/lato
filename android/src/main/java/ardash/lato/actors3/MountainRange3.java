package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Camera3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.screens.GameScreen;
import ardash.lato.weather.EnvColors;

public class MountainRange3 extends Group3D {

    public static final float MOUNT_SIZE = GameScreen.WORLD_WIDTH * 1.19275f;//0.275f;
    /**
     * number of mountains in the mountain range
     */
    private final int numPieces;
    private final float STRETCH_X;
    private final float distanceBetweenPieces = MOUNT_SIZE * 0.77f; //0.77 of above
    private final float VARIANCE = (MOUNT_SIZE + distanceBetweenPieces) / 16f; //(avg of 2 val above) / 4
    private final Color MOUNT_COLOR = new Color(68 / 255f, 145 / 255f, 140 / 255f, 1f);
    /**
     * speed in units per sec
     */
    private float speed = 0f;
    private Color ambientColor = EnvColors.DAY.ambient;

    public MountainRange3(int numPieces, float stretchX) {
        this.numPieces = numPieces;
        this.STRETCH_X = stretchX;
        // we'll make copies of this master-mountain :-)
        Image3D masterimg = new Image3D(1, 1, MOUNT_COLOR, new ModelBuilder()) {
            private Vector3 position = new Vector3();

            @Override
            public boolean isCulled(Camera3D cam) {
                // mountains have special culling algorithm
                this.transform.getTranslation(position);
                return !cam.frustum.pointInFrustum(position);
            }
        };
        for (int i = 0; i < numPieces; i++) {
//			Triangle3D img = new Triangle3D(new Vector3(0, 0, 0), Color.WHITE,new Vector3(1, 0, 0), Color.WHITE,new Vector3(0, 1, 0), Color.WHITE, null);
            Image3D img = new Image3D(1, 1, masterimg) {
                private Vector3 position = new Vector3();

                @Override
                public boolean isCulled(Camera3D cam) {
                    // mountains have special culling algorithm
                    this.transform.getTranslation(position);
                    if (cam.frustum.pointInFrustum(position))
                        return false;
                    position.x += MOUNT_SIZE;
                    if (cam.frustum.pointInFrustum(position))
                        return false;
                    position.x -= MOUNT_SIZE * 2f;
                    if (cam.frustum.pointInFrustum(position))
                        return false;
                    return true;
                }
            };
            img.rotateYaw(45f + 180f);
            img.rotateYaw(MathUtils.random(-2f, 2f));
            img.setScale(MOUNT_SIZE);
            img.translate(distanceBetweenPieces * i + MathUtils.random(-VARIANCE, VARIANCE), MathUtils.random(-VARIANCE, VARIANCE), 3 + (i % 2) * 0.01f);
//			img.translate(MOUNT_SIZE*i,0, 0);
//			img.rotateYaw(20);
            addActor(img);
            img.setName("Mountain" + i);
//			img.setColor(ambientColor);
        }
        setScale(STRETCH_X, 1f, 1f);  // stretch all sidewards: long mountains
//		rotateYaw(45f);
//		scale(0.00500f);
//		translate(60, -60, 0);

        // TODO bring the fog sprites back
//		// add gradiant fog at bottom
//		Image img = new Image(getAssets().getSTexture(SceneTexture.MOUNTAINFOG));
//		img.setSize(MOUNT_SIZE*numPieces, MOUNT_SIZE*1.4f);
//		addActor(img);
//		img.setName("groundFogBelowMountains");
//		img.setTouchable(Touchable.disabled);
//
////		brighter clouds missing at bottom. otherwise blue-ish clouds too visible, see screenshots
//		Image imgBrightClouds = new Image(getAssets().getSTexture(SceneTexture.MOUNTAINFOG));
//		imgBrightClouds.setSize(MOUNT_SIZE*numPieces, MOUNT_SIZE*1.4f);
//		addActor(imgBrightClouds);
//		imgBrightClouds.setName("groundFogBelowMountains");
//		imgBrightClouds.setTouchable(Touchable.disabled);
//		imgBrightClouds.moveBy(0, -5f);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//		int i = 0;
//		translate(0.1f, 0, 0);
//		rotateYaw(1.1f);
//		scale(0.01f, 0, 0);
        for (int i = 0; i < numPieces; i++) {
            final Actor3D child = getChild(i);
            child.translate(-speed * delta, 0, 0);

            // all mountains will eventually move beyond -0. If that happens too far, send them back to the end.
            if (child.getX() < -MOUNT_SIZE / 2f) {
                child.translate(distanceBetweenPieces * numPieces, 0, 0);
            }

        }
    }

}
