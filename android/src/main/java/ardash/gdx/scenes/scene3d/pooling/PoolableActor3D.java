package ardash.gdx.scenes.scene3d.pooling;

import com.badlogic.gdx.graphics.g3d.Model;

import ardash.gdx.scenes.scene3d.Actor3D;

public class PoolableActor3D extends Actor3D implements Initable {

    boolean initialised = false;

    public PoolableActor3D() {
        super();
    }

    public PoolableActor3D(Model model, float x, float y, float z) {
        super(model, x, y, z);
    }

    public PoolableActor3D(Model model) {
        super(model);
    }

//	@Override
//	public boolean remove() {
//		boolean canBeFree = false;
//		if (hasParent())
//			canBeFree = true;
//
//		boolean b = super.remove();
//		if (canBeFree)
//			Pools.free(this);
//
//		return b;
//	}

    @Override
    public void reset() {
        initialised = false;
        setPosition(0f, 0f, 0f);
    }

    @Override
    public void init() {
        initialised = true;
    }

    @Override
    public boolean isInitialized() {
        return initialised;
    }

    @Override
    public void act(float delta) {
        if (!hasParent())
            // This must be checked, because sometimes the Actor get removed, but is still in the loops of the parent to act(). if act() is called after being removed ADN freed, the verification fails.
            return;
        super.act(delta);
        verify();
    }

}
