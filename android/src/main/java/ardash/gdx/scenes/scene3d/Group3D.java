package ardash.gdx.scenes.scene3d;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;


public class Group3D extends Actor3D {
    public static int draw1Count, draw2Count;
    private final SnapshotArray<Actor3D> children = new SnapshotArray<>(true, 4, Actor3D.class);

    public Group3D() {
        super();
        setScale(1, 1, 1);
    }

    public Group3D(Model model) {
        super(model);
        setScale(1, 1, 1);
    }

    public void act(float delta) {
        super.act(delta);
        Actor3D[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            actors[i].act(delta);
        }
        children.end();
    }

    /**
     * draws only the actors with a certain tag
     *
     * @param modelBatch
     * @param environment
     * @param tag
     */
    public void draw(ModelBatch modelBatch, Environment environment, Tag tag) {
        SnapshotArray<Actor3D> children = this.children;
        Actor3D[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            Actor3D child = actors[i];
            if (!child.isVisible()) continue;

            if (tag != null) {
                final Tag childtag = child.getTag();
                if (childtag == null)
                    throw new RuntimeException("Error: This stage was asked to be drawn in 3 grounds, but one actor (named: " + child.getName() + ") in the child-list, has been found without tag. That is an error as it would never be drawn. Assign it to Center, front or back.");
                if (!tag.equals(childtag))
                    continue;
            }

            // update child's matrix
            child.transform.setToTranslationAndScaling(child.x, child.y, child.z, child.scaleX, child.scaleY, child.scaleZ);
            // origin can be added here (if in use

            if (child.originX != 0f || child.originY != 0f) {
                child.transform.translate(-child.originX, -child.originY, 0f);
                child.transform.mul(child.rotationMatrix);
                child.transform.translate(child.originX, child.originY, 0f);
            } else {
                child.transform.mul(child.rotationMatrix);
            }

            child.transform.mulLeft(transform);

            draw1Count++; // count all that are meant to be drawn without culling
            final Camera3D camera = (Camera3D) getStage().getCamera();
            if (child.isCulled(camera))
                continue;

            child.draw(modelBatch, environment);
            draw2Count++; // count all that are have been drawn after culling
        }
        children.end();

        // call debug method since we don't call super.draw() here
        drawDebug(modelBatch, environment);

    }

    /**
     * Draws the group and its children.
     */
    @Override
    public void draw(ModelBatch modelBatch, Environment environment) {
        this.draw(modelBatch, environment, null);
    }

    /**
     * Adds an actor as a child of this group. The actor is first removed from its parent group, if any.
     *
     * @see #remove()
     */
    public void addActor(Actor3D actor) {
        actor.remove();
        children.add(actor);
        actor.setParent(this);
        actor.setStage(getStage());
        childrenChanged();
    }

    /**
     * Removes an actor from this group. If the actor will not be used again and has actions, they should be
     * {@link Actor3D#clearActions() cleared} so the actions will be returned to their
     * {@link Action#setPool(com.badlogic.gdx.utils.Pool) pool}, if any. This is not done automatically.
     */
    public boolean removeActor(Actor3D actor) {
        if (!children.removeValue(actor, true)) return false;
        actor.setParent(null);
        actor.setStage(null);
        childrenChanged();
        return true;
    }

    /**
     * Called when actors are added to or removed from the group.
     */
    protected void childrenChanged() {
    }

    /**
     * Removes all actors from this group.
     */
    public void clearChildren() {
        Actor3D[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            Actor3D child = actors[i];
            child.setStage(null);
            child.setParent(null);
        }
        children.end();
        children.clear();
        childrenChanged();
    }

    /**
     * Removes all children, actions, and listeners from this group.
     */
    public void clear() {
        super.clear();
        clearChildren();
    }

    /**
     * Returns the first actor found with the specified name. Note this recursively compares the name of every actor in the group.
     */
    public Actor3D findActor(String name) {
        Array<Actor3D> children = this.children;
        for (int i = 0, n = children.size; i < n; i++)
            if (name.equals(children.get(i).getName())) return children.get(i);
        for (int i = 0, n = children.size; i < n; i++) {
            Actor3D child = children.get(i);
            if (child instanceof Group3D) {
                Actor3D actor = ((Group3D) child).findActor(name);
                if (actor != null) return actor;
            }
        }
        return null;
    }

    @Override
    protected void setStage(Stage3D stage) {
        super.setStage(stage);
        Array<Actor3D> children = this.children;
        for (int i = 0, n = children.size; i < n; i++)
            children.get(i).setStage(stage);
    }

    /**
     * Returns an ordered list of child actors in this group.
     */
    public SnapshotArray<Actor3D> getChildren() {
        return children;
    }

    public Actor3D getChild(int i) {
        return children.get(i);
    }

    public boolean hasChildren() {
        return children.size > 0;
    }

    /**
     * If true, {@link Actor3D#drawDebug(ModelBatch, Environment)} will be called for this group and, optionally, all children recursively.
     */
    public void setDebug(boolean enabled, boolean recursively, ModelBuilder modelBuilder) {
        setDebug(enabled, modelBuilder);
        if (recursively) {
            for (Actor3D child : children) {
                if (child instanceof Group3D) {
                    ((Group3D) child).setDebug(enabled, true, modelBuilder);
                } else {
                    child.setDebug(enabled, modelBuilder);
                }
            }
        }
    }

    public void setDebug(boolean enabled, boolean recursively) {
        setDebug(enabled, recursively, new ModelBuilder());
    }

    /**
     * Prints the actor hierarchy recursively for debugging purposes.
     */
    public void print() {
        print("");
    }

    private void print(String indent) {
        Actor3D[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            if (actors[i] instanceof Group3D) ((Group3D) actors[i]).print(indent + "|  ");
        }
        children.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Actor3D actor3D : children)
            actor3D.dispose();
    }

    @Override
    public boolean isCulled(Camera3D cam) {
        return false;
    }

}
