

package ardash.gdx.scenes.scene3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class Camera3D extends PerspectiveCamera {

    public Camera3D() {
        this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public Camera3D(float viewportWidth, float viewportHeight) {
        this(67, viewportWidth, viewportHeight);
    }

    public Camera3D(float fieldOfViewY, float viewportWidth, float viewportHeight) {
        super(fieldOfViewY, viewportWidth, viewportHeight);
        near = 0.1f;
        far = 300f;
        update();
    }

    private float offsetX = 10f, offsetY = 10f, offsetZ = 10f;
    private float followSpeed = 0.5f;
    private Actor3D followedActor;
    private boolean lookAt;

    /**
     * The camera follows the actor as it moves along the scene
     *
     * @param actor3D The actor the camera has to follow , if it is null the camera stops following
     * @param lookAt  whether the camera should always be pointing to the actor
     */
    public void followActor(Actor3D actor3D, boolean lookAt) {
        followedActor = actor3D;
        this.lookAt = lookAt;
    }

    /**
     * This sets the distance between the camera and the actor
     *
     * @param offX the x distance from actor
     * @param offY the y distance from actor
     * @param offZ the z distance from actor
     */
    public void followOffset(float offX, float offY, float offZ) {
        offsetX = offX;
        offsetY = offY;
        offsetZ = offZ;
    }

    private float moveDuration;
    private float moveTime;
    private boolean moveCompleted;
    private float moveLastPercent;
    private float panSpeedX, panSpeedY, panSpeedZ;
    private float movePercentDelta;

    private float rotateTime;
    private float rotateDuration;
    private float rotateYaw, rotatePitch, rotateRoll;
    private boolean rotateCompleted;
    private float rotateLastPercent;
    private float rotatePercentDelta;

    public void moveTo(float x, float y, float z, float duration) {
        moveBy(x - position.x, y - position.y, z - position.z, duration);
    }

    public void moveBy(float amountX, float amountY, float amountZ, float duration) {
        moveDuration = duration;
        panSpeedX = amountX;
        panSpeedY = amountY;
        panSpeedZ = amountZ;
        moveLastPercent = 0;
        moveTime = 0;
        moveCompleted = false;
    }

    public void rotateBy(float yaw, float pitch, float roll, float duration) {
        rotateLastPercent = 0;
        rotateTime = 0;
        rotateYaw = yaw;
        rotatePitch = pitch;
        rotateRoll = roll;
        rotateDuration = duration;
        rotateCompleted = false;
    }

    @Override
    public void update() {
        super.update();
        float delta = Gdx.graphics.getDeltaTime();
        if (!moveCompleted) {
            moveTime += delta;
            moveCompleted = moveTime >= moveDuration;
            float percent;
            if (moveCompleted)
                percent = 1;
            else {
                percent = moveTime / moveDuration;
            }
            movePercentDelta = percent - moveLastPercent;
            translate(panSpeedX * movePercentDelta, panSpeedY * movePercentDelta, panSpeedZ * movePercentDelta);
            moveLastPercent = percent;
        }
        if (!rotateCompleted) {
            rotateTime += delta;
            rotateCompleted = rotateTime >= rotateDuration;
            float percent;
            if (rotateCompleted)
                percent = 1;
            else
                percent = rotateTime / rotateDuration;
            rotatePercentDelta = percent - rotateLastPercent;
            rotate(Vector3.Z, rotateYaw * rotatePercentDelta);
            rotate(Vector3.Y, rotatePitch * rotatePercentDelta);
            rotate(Vector3.X, rotateRoll * rotatePercentDelta);
            rotateLastPercent = percent;
        }
        if (followedActor != null) {
            moveTo(followedActor.x + offsetX, followedActor.y + offsetY, followedActor.z + offsetZ, followSpeed);
            if (lookAt)
                lookAt(followedActor.x, followedActor.y, followedActor.z);
        }
    }


    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getZ() {
        return position.z;
    }

    public float getWidth() {
        return viewportWidth;
    }

    public float getHeight() {
        return viewportHeight;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public float getFar() {
        return far;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getNear() {
        return near;
    }

    public void setFieldOfView(float fov) {
        fieldOfView = fov;
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

}
