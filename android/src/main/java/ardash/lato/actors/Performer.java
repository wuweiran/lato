

package ardash.lato.actors;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.bitfire.postprocessing.effects.Zoomer;
import com.bitfire.postprocessing.filters.RadialBlur.Quality;

import ardash.gdx.graphics.g3d.ParticleEmitter;
import ardash.gdx.graphics.g3d.ParticleEmitter.ParticleEmitterType;
import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Camera3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.actions.Actions3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;
import ardash.lato.A.MusicAsset;
import ardash.lato.actions.Actions;
import ardash.lato.actions.GravityAction;
import ardash.lato.screens.GameOverDialog;
import ardash.lato.weather.AmbientColorChangeListener;

public class Performer extends Group3D implements Disposable, AmbientColorChangeListener {
    public enum Pose {
        RIDE, DUCK, JUMP, CRASH_ASS, CRASH_NOSE//, ROLL, FLY, CRASHED, GRIND
    }

    public enum Demise {
        NONE, LAND_ON_ASS, LAND_ON_NOSE, LAND_ON_STONE, HIT_STONE, DROP_IN_CANYON;
    }

    private static final float ROTATION_SPEED = 180f; // TODO (deg/sec) this could be different for different performers or boards
    private static final float PERFORMER_WIDTH = 1.85f;
    public static final float MIN_SPEED = 9.3f;
    private static final float MAX_SPEED = 29.3f;
    private static final float MIN_CAM_SPOT_X = 10f;
    private static final float MAX_CAM_SPOT_X = 24f;
    private static final float JUMP_FORCE = PERFORMER_WIDTH * 1.9f;

    private float speed = 0f; // speed in m/s
    private float runtime = 0f; // lifetime starting after game started
    private boolean isUserInputDown = false;
    //	private float direction = 0f; // current rotation (direction) in degrees
    private final Vector2 velocity = new Vector2(); // this is only here to safe new-calls
    protected ParticleEmitter spray = new ParticleEmitter(ParticleEmitterType.SNOW);
    private final List<PerformerListener> listeners = new ArrayList<PerformerListener>();
    private final Map<Pose, Image3D> poses = new EnumMap<Pose, Image3D>(Pose.class);
    protected Pose pose = Pose.RIDE;
    protected PlayerState state = PlayerState.INIT;
    private Demise causeOfDeath = Demise.NONE;

    private final Vector2 scarfAttachPoint = new Vector2(0, 0);

    /**
     * vertical speed is intentionally not in a vector with 'speed' because the velocity is handled differently
     * depending on if actor is in air or on ground. Physics on ground are not realistic to improve gameplay.
     */
    private float vspeed = 0f; // speed in m/s

    /**
     * A spot in front of the actor, where he wants the camera to look at. Usually a few meters in front of the actor.
     */
    private Vector2 camSpot = new Vector2();
    //	public int currentContacts = 0;
    private float airTime;
    private float timeInState;
    private float startedAtX = -1;
    public Rectangle bb;

    public interface PerformerListener {
        void onPositionChange(float newX, float newY);

        void onSpeedChanged(float newSpeed, float percentage);
    }

    public Performer() {
        ModelBuilder mb = new ModelBuilder();
        setName("Performer");
        setTag(Tag.CENTER);
        for (Pose pose : Pose.values()) {
            String performer = "P1";
            final String posename = performer + "_" + pose.name().toUpperCase();
            final AtlasRegion poseTextureRegion = A.getTextureRegion(ARAsset.valueOf(posename));
            Image3D img = new Image3D(PERFORMER_WIDTH, PERFORMER_WIDTH, poseTextureRegion, mb);
            img.setName(posename);
            addActor(img);
            poses.put(pose, img);
//			setDebug(true, true);
//			img.setDebug(true);
        }
        setPose(Pose.CRASH_ASS);
        setOriginX(-PERFORMER_WIDTH / 2f);
        camSpot.set(getX(), getY() + 10f);
        setSpeed(MIN_SPEED);

        Image3D ambientColorContainer = new Image3D(1, 1, new Color(), new ModelBuilder());
        addActor(ambientColorContainer);
        ambientColorContainer.setVisible(false);

//		scarfAttachPointGroup.setPosition(0.5f, 0.1f);
        Group3D scarfAttachPointGroup = new Group3D();
        scarfAttachPointGroup.setPosition(0f, 0f);

        this.bb = new Rectangle(getX(), getY(), PERFORMER_WIDTH * 0.9f, PERFORMER_WIDTH * 0.9f);
    }

    //	private float accum = 0;
//	private float step = 1/60f;
//
    @SuppressWarnings("deprecation")
    public void act(float delta) {
        final float previousX = getX();
        final float previousY = getY();
//		accum += delta;
//		while (accum >= step) {
//			System.out.println("d : "+state);
        super.act(delta);
        if (state.isStarted()) {
            runtime += delta;
            timeInState += delta;
        }

        if (state == PlayerState.DROPPED) {
            return;
        }

        // rotation for the forward movement (ignored when in air)
        final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();

        if (state.isInAir()) {
            // apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
            moveBy(speed * delta, 0); // movement is product of time-delta and speed-delta

            // linear damping in air, otherwise the player is floating to far forward (unrealistic - he doesn't have a wing glider)
            setSpeed(speed - (1.1f * delta));

            setOriginY(-PERFORMER_WIDTH / 2f);

            //register landing
            final float heightUnderActorBeforeForwardMovement = getGameScreen().waveDrawer.getHeightAt(getX() + (PERFORMER_WIDTH / 2f));
            if (getY() < heightUnderActorBeforeForwardMovement) {
//				System.out.println("hua: " +heightUnderActorBeforeForwardMovement+ " , Y: "+getY());

                // land only if in air since longer time
                if (timeInState >= 0.1f) {
                    land();
                }
            } else
//			if (heightUnderActor > heightOfMe) // check if hit the ground
//			{
//				land();
//				getActions().clear();
//			}
            {
                // if input touch down rotate counter clockwise, otherwise rotate towards ground
                if (isUserInputDown) {
                    setPose(Pose.RIDE); // TODO set to roll
                    rotateBy(ROTATION_SPEED * delta);
                } else {
                    setPose(Pose.JUMP);
                    float direction = rotation > 180 ? 1 : -1;
                    rotateBy(ROTATION_SPEED * 0.3f * direction * delta);
                }

            }
        } else {
            // apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
            velocity.set(1, 1).setLength(speed).setAngle(state.isInAir() ? 0f : rotation);
            final float deltaX = velocity.x;
            moveBy(deltaX * delta, 0); // movement is product of time-delta and speed-delta

            // rotation point is at the feet when actor is on the ground
            setOriginY(0);

            // we use setPosition instead of setY(), so setPosition can be overwritten for smoother movement
            // set the height of the terrain under the actor if not in air
            float heightUnderActor = getGameScreen().waveDrawer.getHeightAt(getX() + (PERFORMER_WIDTH / 2f));

            // check the the offset is very high, if the actor would suddenly fall, make him 0-jump : airborne + gravity
            final float heightDelta = getY() - heightUnderActor;

            if ((heightDelta >= -1.729992 && heightDelta < 1.729992f) || !state.isStarted()) {
                // all good, just put him on the ground
                setPosition(getX(), heightUnderActor);
                // set rotation to what the ground is under the actor
                final float newRot = getGameScreen().waveDrawer.getAngleAtX(getX() + (PERFORMER_WIDTH / 2f));

                // apply only reasonable values 275-360 and 0-85
                if ((newRot > 0 && newRot < 89) || (newRot > 271 && newRot < 360))
                    setRotation(newRot);
            } else if (heightDelta < 0) {
                // Terrain goes suddenly up (inside canyon) - we don't put walls like this on the terrain
                // this cannot happen anymore withthe AbyssCollider
//				drop(); // TODO make new method drop() similar to crash(). use new status, move the player slightly backwards
            } else {
                // Terrain goes suddenly down (canyon, ramp)
                jump(0f);
            }


            // accelerate on ground
            final float angleToGround = 360f - velocity.angle(); // 0 or 360 is horizontal, 90 is downward, 45 is ramp down forward
//			System.out.println(angleToGround);

            if (!state.isCrashed()) {
                if (angleToGround > 0) {
                    if (angleToGround < 20f) // TODO adjust here. everything above this angle speeds up
                    {
                        setSpeed(speed - (1.1f * delta));
                    } else if (angleToGround < 90f) {
                        setSpeed(speed + (5.1f * delta));
                    } else {
                        setSpeed(speed - (1.1f * delta));
                    }
                }

                if (isUserInputDown) {
                    jump(JUMP_FORCE);
                }
            } else {
                // if crashed: break hard
                setSpeed(speed - (15.1f * delta));
            }

        }
//		//register landing
//		final float heightUnderActorBeforeForwardMovement = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
//		if (state.isInAir() && getY()<heightUnderActorBeforeForwardMovement)
//		{
//			land();
//			getActions().clear();
//		}
//
//		// rotation for the forward movement (ignored when in air)
//		final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();
//
//		// apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
//		velocity.set(1,1).setLength(speed).setAngle(state.isInAir() ? 0f : rotation);
//		final float deltaX = velocity.x;
//		moveBy(deltaX*delta, 0); // movement is product of time-delta and speed-delta
//
//		// accelerate on ground
//		if (! state.isInAir())
//		{
//			final float angleToGround = 360f - velocity.angle(); // 0 or 360 is horizontal, 90 is downward, 45 is ramp down forward
////			System.out.println(angleToGround);
//			if (angleToGround > 0)
//			{
//				if (angleToGround < 20f) // TODO adjust here. everything above this angle speeds up
//				{
//					setSpeed(speed-(1.1f*delta));
//				}
//				else if (angleToGround < 90f)
//				{
//					setSpeed(speed+(5.1f*delta));
//				}
//				else
//				{
//					setSpeed(speed-(1.1f*delta));
//				}
//			}
//		}
//
//		// at this stage the actor is already moved slightly forward and the speed is adjusted
//
//
////		final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();
//
//		float heightUnderActor = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
//		float heightOfMe = getY();
//		if (! state.isInAir())
//		{
////			fake jump for sudden abyss
////			vspeed = heightOfMe - heightUnderActor;
////			if (vspeed > 0.2f)
////			{
//////				jump(0f);
//////				return;
////			}
//
//			// rotation point is at the feet when actor is on the ground
//			setOriginY(0);
//
//			// we use setPosition instead of setY(), so setPosition can be overwritten for smoother movement
//			// set the height of the terrain under the actor if not in air
//			setPosition(getX(), heightUnderActor);
//
//
//			// set rotation to what the ground is under the actor
//			setRotation( getGameScreen().waveDrawer.getAngleAtX(getX()+(PERFORMER_WIDTH/2f)));
//
//			// TODO continue review here
//			if (isUserInputDown)
//			{
//				jump(JUMP_FORCE);
//			}
//
//		}
//		else
//		{
//			setOriginY(-PERFORMER_WIDTH/2f);
//
////			//register landing
////			final float heightUnderActorBeforeForwardMovement = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
////			if (state.isInAir() && getY()<heightUnderActorBeforeForwardMovement)
////			{
////				land();
////				getActions().clear();
////			}
//			if (heightUnderActor > heightOfMe) // check if hit the ground
//			{
//				land();
//				getActions().clear();
//			}
//
//			// if input touch down rotate counter clockwise, otherwise rotate towards ground
//			if (isUserInputDown)
//			{
//				setPose(Pose.RIDE); // TODO set to roll
//				rotateBy(ROTATION_SPEED*delta);
//			}
//			else
//			{
//				setPose(Pose.JUMP);
//				float direction = rotation > 180 ? 1 : -1;
//				rotateBy(ROTATION_SPEED*0.3f*direction*delta);
//			}
//		}

//		 accum -= step;

        if (!state.isCrashed()) {
            float newCamSpotX = MathUtils.lerp(MIN_CAM_SPOT_X, MAX_CAM_SPOT_X, getSpeedPercentage());
            newCamSpotX = MathUtils.clamp(newCamSpotX, MIN_CAM_SPOT_X, MAX_CAM_SPOT_X);
            final Vector2 newCamSpot = new Vector2(getX() + newCamSpotX, getY());
            if (getSpeed() == 0) {
                newCamSpot.y += 5f; // initially when standing, move cam above
            }

            // before applying the new camspot, check if the difference is too big and go there smoothly
            final Vector2 diff = newCamSpot.cpy().sub(camSpot);
            diff.clamp(0, getMaxCamSpeed());
            camSpot.add(diff);

        }

//		camSpot.set(newCamSpot);

        // inform listeners about new position
//		System.out.println("Performer is at: "+ getX() + ","+ getY());
        for (PerformerListener listener : listeners) {
            listener.onPositionChange(getX(), getY());

            if (!spray.hasParent())
                getStage().addActor(spray);
            spray.setPosition(getX(), getY());
            bb.setPosition(getX(), getY());
        }

    }

    private float getMaxCamSpeed() {
        return state.isStarted() ? (runtime > 1f ? 3.3f : 03.3f) : 0.03f;
    }

    public void setPose(Pose pose) {
        this.pose = pose;
        for (Actor3D a : getChildren()) {
            a.setVisible(false);
        }
        poses.get(pose).setVisible(true);
    }

    public Pose getPose() {
        return pose;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        if (!state.isStarted())
            return;

        if (speed <= 0f && state.isCrashed()) {
            this.speed = 0f;
            return;
        }

        if (speed < MIN_SPEED && !state.isCrashed())
            speed = MIN_SPEED;
        if (speed > MAX_SPEED)
            speed = MAX_SPEED;
        this.speed = speed;

        // inform listeners
        for (PerformerListener l : listeners) {
            l.onSpeedChanged(speed, getSpeedPercentage());
        }
    }

    public float getSpeedPercentage() {
        final float max = MAX_SPEED - MIN_SPEED;
        final float cur = speed - MIN_SPEED;

        if (cur <= 0)
            return 0f;
        if (cur >= MAX_SPEED)
            return 1f;

        return cur / max;
    }

    public Vector2 getCamSpot() {
        return camSpot;
    }

    @Override
    public void onAmbientColorChangeTriggered(Color target, float seconds) {
//		ambientColorContainer.addAction(Actions.color(target, seconds));
//
//		// performer is also emitting ambient light in ambient color
//		target = target.cpy();
//		target.mul(4f); // mul == brighter ==> so the actor doesn't become black but just a bit darker
//		getChild(0).addAction(Actions.color(target, seconds));
    }

    /**
     * handle the only possible user input (on the game stage): touch anywhere on the screen
     *
     * @param touchDown touch up or touch down
     */
    public void userInput(boolean touchDown) {
        if (!state.isStarted()) {
            startedAtX = getX();
            setState(PlayerState.SLIDING);
            setPose(Pose.RIDE);
            return; // don't jump or rotate if game not started yet
//			setSpeed(MIN_SPEED);
//			getGameManager().setStarted(true);
        }

        isUserInputDown = touchDown;
//		if (!state.isInAir())
//		{
//			if (touchDown)
//			{
////				jump(2f);
//			}
//		}

    }

    @Override
    public float getRotation() {
        float rotation = super.getRotation();
        rotation %= 360f;
        if (rotation < 0f)
            rotation = 360 + rotation;
        return rotation;
    }

    public void jump(float jumpforce) {
        System.out.println("jump : " + isUserInputDown);
        setState(PlayerState.INAIR);
        getActions().clear();
        final float jumpDuration = jumpforce == 0f ? 0f : 0.3f;
        addAction(Actions.sequence(
            Actions.moveBy(0, jumpforce, 0, jumpDuration, Interpolation.exp5Out),
//				Actions.moveBy(0, jumpforce, 0, 0.3f, Interpolation.circleOut),
            new GravityAction()
//				Actions.gravity()
        ));
//		setY(getY()+jumpforce);
//		setPose(Pose.JUMP);

//		getBody().applyForceToCenter(100f, 100f, true);
        if (jumpforce > 0f) {
////			getBody().applyLinearImpulse(10, jumpforce, 0, 0, true);
//			getBody().setType(BodyType.DynamicBody);
//			Vector2 imp = velocity.cpy();
//			getBody().applyLinearImpulse(imp, new Vector2(), true);
////			imp = imp.angle() < 180f ? imp.rotate(-45f):imp.rotate(45f);
//			imp = imp.rotate(90f).nor();
//			imp.scl(jumpforce);
//			getBody().applyLinearImpulse(imp, new Vector2(), true);
        }
    }

    /**
     * touching down after a jump or fall
     */
    private void land() {
        System.out.println("land()");
        clearActions();
        final float rotation = getRotation();

        if (rotation >= 40f && rotation <= 190f) {
            crash(Pose.CRASH_ASS);
            setCauseOfDeath(Demise.LAND_ON_ASS);
        } else if (rotation >= 190f && rotation <= 310f) {
            crash(Pose.CRASH_NOSE);
            setCauseOfDeath(Demise.LAND_ON_NOSE);
        } else {
            setState(PlayerState.DUCKING);
            setPose(Pose.DUCK);
        }

        // when jumping and keeping it pressed, the "press" will continue to act and rotate the actor
        // on the other hand: when landing, while the screen is pressed it must not be registered as touch-down
        // so lets unregister the last touch down, in order to wait for the next touch down
        // this is important ie. for very close landing where user needs to touch down until last moment
        // but in no case we want "bouncing" because screen is still touched. A new jump requires a new touch.
        userInput(false);
    }

    /**
     * This must be public. A crash can be triggered internally (ie. by landing nt on feet) and externally (ie. by hitting a stone)
     *
     * @param crashPose
     */
    public void crash(Pose crashPose) {
        userInput(false);
        setState(PlayerState.CRASHED);
        setPose(crashPose);
        addAction(Actions3D.sequence(
            Actions3D.delay(2f),
            Actions3D.run(new Runnable() {
                @SuppressWarnings("SuspiciousIndentation")
                @Override
                public void run() {
                    new GameOverDialog(getCauseOfDeath(), getTraveledDistanceMeters()).show(getGameScreen().guiStage);

                    //blur background behind dialog
                    Zoomer sb = new Zoomer((int) (Gdx.graphics.getWidth() * 0.25f), (int) (Gdx.graphics.getHeight() * 0.25f), Quality.VeryHigh);
                    sb.setBlurStrength(2);
                    getGameScreen().postProcessor.addEffect(sb);

                    // play sad music
//						SoundPlayer.swapMusicTo(MusicAsset.SAD);
                    MusicProvider.getInstance().fadeToMusic(A.getMusic(MusicAsset.SAD));

                }
            })
        ));
    }

    public void drop() {
        System.out.println("drop()");
//		getActions().clear();
        crash(Pose.CRASH_ASS);
        setState(PlayerState.DROPPED);
//		setSpeed(-1f);
//		addAction(new GravityAction());// he can only hit the abyss-collider when already falling
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        if (this.state.equals(state))
            return;
        timeInState = 0f;
        this.state = this.state.moveTo(state);
        if (state == PlayerState.SLIDING || state == PlayerState.DUCKING) {
            spray.startEmitting();
        } else {
            spray.stopEmitting();
        }
    }

    public float getTimeInState() {
        return timeInState;
    }

    public void addListener(PerformerListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean isCulled(Camera3D cam) {
        return false;
    }

    @Override
    public void dispose() {
//		Disposables.gracefullyDisposeOf(snowSpray);
    }

    @Override
    public float getWidth() {
        return PERFORMER_WIDTH;
    }

    @Override
    public float getHeight() {
        return PERFORMER_WIDTH;
    }

    public Vector2 getScarfAttachPointInStageCoords() {
        if (state == PlayerState.INAIR) {
            scarfAttachPoint.set(0.1f, 0.3f);
        } else {
            scarfAttachPoint.set(0.15f, 0.9f);
        }
//		scarfAttachPointGroup.localToParentCoordinates(scarfAttachPoint);
        this.localToParentCoordinates(scarfAttachPoint);
//		System.out.println(scarfAttachPoint);
        return scarfAttachPoint;
    }

    public int getTraveledDistanceMeters() {
        if (!state.isStarted())
            return 0;
        float dist = getX() - startedAtX;
        return (int) dist;
    }

    public void setCauseOfDeath(Demise causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public Demise getCauseOfDeath() {
        return causeOfDeath;
    }
}
