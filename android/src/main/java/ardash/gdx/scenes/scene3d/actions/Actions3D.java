package ardash.gdx.scenes.scene3d.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import ardash.gdx.scenes.scene3d.Action3D;
import ardash.gdx.scenes.scene3d.Actor3D;

public class Actions3D {

    /**
     * Returns a new or pooled action of the specified type.
     */
    static public <T extends Action3D> T action3d(Class<T> type) {
        Pool<T> pool = Pools.get(type);
        T action = pool.obtain();
        action.setPool(pool);
        return action;
    }

    static public AddAction addAction(Action3D action) {
        AddAction addAction = action3d(AddAction.class);
        addAction.setAction(action);
        return addAction;
    }

    static public AddAction addAction(Action3D action, Actor3D targetActor) {
        AddAction addAction = action3d(AddAction.class);
        addAction.setTarget(targetActor);
        addAction.setAction(action);
        return addAction;
    }

    static public RemoveAction removeAction(Action3D action) {
        RemoveAction removeAction = action3d(RemoveAction.class);
        removeAction.setAction(action);
        return removeAction;
    }

    static public RemoveAction removeAction(Action3D action, Actor3D targetActor) {
        RemoveAction removeAction = action3d(RemoveAction.class);
        removeAction.setTarget(targetActor);
        removeAction.setAction(action);
        return removeAction;
    }

    /**
     * Moves the actor instantly.
     */
    static public MoveToAction moveTo(float x, float y, float z) {
        return moveTo(x, y, z, 0, null);
    }

    static public MoveToAction moveTo(float x, float y, float z, float duration) {
        return moveTo(x, y, z, duration, null);
    }

    static public MoveToAction moveTo(float x, float y, float z, float duration, Interpolation interpolation) {
        MoveToAction action = action3d(MoveToAction.class);
        action.setPosition(x, y, z);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Moves the actor instantly.
     */
    static public MoveByAction moveBy(float amountX, float amountY, float amountZ) {
        return moveBy(amountX, amountY, amountZ, 0, null);
    }

    static public MoveByAction moveBy(float amountX, float amountY, float amountZ, float duration) {
        return moveBy(amountX, amountY, amountZ, duration, null);
    }

    static public MoveByAction moveBy(float amountX, float amountY, float amountZ, float duration, Interpolation interpolation) {
        MoveByAction action = action3d(MoveByAction.class);
        action.setAmount(amountX, amountY, amountZ);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Scales the actor instantly.
     */
    static public ScaleToAction scaleTo(float x, float y, float z) {
        return scaleTo(x, y, z, 0, null);
    }

    static public ScaleToAction scaleTo(float x, float y, float z, float duration) {
        return scaleTo(x, y, z, duration, null);
    }

    static public ScaleToAction scaleTo(float x, float y, float z, float duration, Interpolation interpolation) {
        ScaleToAction action = action3d(ScaleToAction.class);
        action.setScale(x, y, z);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Scales the actor instantly.
     */
    static public ScaleByAction scaleBy(float amountX, float amountY, float amountZ) {
        return scaleBy(amountX, amountY, amountZ, 0, null);
    }

    static public ScaleByAction scaleBy(float amountX, float amountY, float amountZ, float duration) {
        return scaleBy(amountX, amountY, amountZ, duration, null);
    }

    static public ScaleByAction scaleBy(float amountX, float amountY, float amountZ, float duration, Interpolation interpolation) {
        ScaleByAction action = action3d(ScaleByAction.class);
        action.setAmount(amountX, amountY, amountZ);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Rotates the actor instantly.
     */
    static public RotateToAction rotateTo(float yaw, float pitch, float roll) {
        return rotateTo(yaw, pitch, roll, 0, null);
    }

    static public RotateToAction rotateTo(float yaw, float pitch, float roll, float duration) {
        return rotateTo(yaw, pitch, roll, duration, null);
    }

    static public RotateToAction rotateTo(float yaw, float pitch, float roll, float duration, Interpolation interpolation) {
        RotateToAction action = action3d(RotateToAction.class);
        action.setRotation(yaw, pitch, roll);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Rotates the actor instantly.
     */
    static public RotateByAction rotateBy(float yaw, float pitch, float roll) {
        return rotateBy(yaw, pitch, roll, 0, null);
    }

    static public RotateByAction rotateBy(float yaw, float pitch, float roll, float duration) {
        return rotateBy(yaw, pitch, roll, duration, null);
    }

    static public RotateByAction rotateBy(float yaw, float pitch, float roll, float duration, Interpolation interpolation) {
        RotateByAction action = action3d(RotateByAction.class);
        action.setAmount(yaw, pitch, roll);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }


    static public VisibleAction show() {
        return visible(true);
    }

    static public VisibleAction hide() {
        return visible(false);
    }

    static public VisibleAction visible(boolean visible) {
        VisibleAction action = action3d(VisibleAction.class);
        action.setVisible(visible);
        return action;
    }

    static public RemoveActorAction removeActor(Actor3D removeActor) {
        RemoveActorAction action = action3d(RemoveActorAction.class);
        action.setRemoveActor(removeActor);
        return action;
    }

    static public FreeActorAction freeActor(Actor3D freeActor) {
        FreeActorAction action = action3d(FreeActorAction.class);
        action.setFreeActor(freeActor);
        return action;
    }

    static public DelayAction delay(float duration) {
        DelayAction action = action3d(DelayAction.class);
        action.setDuration(duration);
        return action;
    }

    static public DelayAction delay(float duration, Action3D delayedAction) {
        DelayAction action = action3d(DelayAction.class);
        action.setDuration(duration);
        action.setAction(delayedAction);
        return action;
    }

    static public TimeScaleAction timeScale(float scale, Action3D scaledAction) {
        TimeScaleAction action = action3d(TimeScaleAction.class);
        action.setScale(scale);
        action.setAction(scaledAction);
        return action;
    }

    static public SequenceAction sequence(Action3D action1) {
        SequenceAction action = action3d(SequenceAction.class);
        action.addAction(action1);
        return action;
    }

    static public SequenceAction sequence(Action3D action1, Action3D action2) {
        SequenceAction action = action3d(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        return action;
    }

    static public SequenceAction sequence(Action3D action1, Action3D action2, Action3D action3) {
        SequenceAction action = action3d(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        return action;
    }

    static public SequenceAction sequence(Action3D action1, Action3D action2, Action3D action3, Action3D action4) {
        SequenceAction action = action3d(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        action.addAction(action4);
        return action;
    }

    static public SequenceAction sequence(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5) {
        SequenceAction action = action3d(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        action.addAction(action4);
        action.addAction(action5);
        return action;
    }

    static public SequenceAction sequence(Action3D... actions) {
        SequenceAction action = action3d(SequenceAction.class);
        for (Action3D action1 : actions) action.addAction(action1);
        return action;
    }

    static public SequenceAction sequence() {
        return action3d(SequenceAction.class);
    }

    static public ParallelAction parallel(Action3D action1) {
        ParallelAction action = action3d(ParallelAction.class);
        action.addAction(action1);
        return action;
    }

    static public ParallelAction parallel(Action3D action1, Action3D action2) {
        ParallelAction action = action3d(ParallelAction.class);
        action.addAction(action1);
        action.addAction(action2);
        return action;
    }

    static public ParallelAction parallel(Action3D action1, Action3D action2, Action3D action3) {
        ParallelAction action = action3d(ParallelAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        return action;
    }

    static public ParallelAction parallel(Action3D action1, Action3D action2, Action3D action3, Action3D action4) {
        ParallelAction action = action3d(ParallelAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        action.addAction(action4);
        return action;
    }

    static public ParallelAction parallel(Action3D action1, Action3D action2, Action3D action3, Action3D action4, Action3D action5) {
        ParallelAction action = action3d(ParallelAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        action.addAction(action4);
        action.addAction(action5);
        return action;
    }

    static public ParallelAction parallel(Action3D... actions) {
        ParallelAction action = action3d(ParallelAction.class);
        for (Action3D action1 : actions) action.addAction(action1);
        return action;
    }

    static public ParallelAction parallel() {
        return action3d(ParallelAction.class);
    }

    static public RepeatAction repeat(int count, Action3D repeatedAction) {
        RepeatAction action = action3d(RepeatAction.class);
        action.setCount(count);
        action.setAction(repeatedAction);
        return action;
    }

    static public RepeatAction forever(Action3D repeatedAction) {
        RepeatAction action = action3d(RepeatAction.class);
        action.setCount(RepeatAction.FOREVER);
        action.setAction(repeatedAction);
        return action;
    }

    static public RunnableAction run(Runnable runnable) {
        RunnableAction action = action3d(RunnableAction.class);
        action.setRunnable(runnable);
        return action;
    }

    static public AfterAction after(Action3D action) {
        AfterAction afterAction = action3d(AfterAction.class);
        afterAction.setAction(action);
        return afterAction;
    }

    /**
     * Sets the actor's color instantly.
     */
    static public ColorAction color(Color color) {
        return color(color, 0, null);
    }

    /**
     * Transitions from the color at the time this action starts to the specified color.
     */
    static public ColorAction color(Color color, float duration) {
        return color(color, duration, null);
    }

    /**
     * Transitions from the color at the time this action starts to the specified color.
     */
    static public ColorAction color(Color color, float duration, Interpolation interpolation) {
        ColorAction action = action3d(ColorAction.class);
        action.setEndColor(color);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Transitions from the alpha at the time this action starts to the specified alpha.
     */
    static public AlphaAction alpha(float a, float duration, @Null Interpolation interpolation) {
        AlphaAction action = action3d(AlphaAction.class);
        action.setAlpha(a);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }

    /**
     * Transitions from the alpha at the time this action starts to an alpha of 0.
     */
    static public AlphaAction fadeOut(float duration) {
        return alpha(0, duration, null);
    }

    /**
     * Transitions from the alpha at the time this action starts to an alpha of 1.
     */
    static public AlphaAction fadeIn(float duration) {
        return alpha(1, duration, null);
    }


}
