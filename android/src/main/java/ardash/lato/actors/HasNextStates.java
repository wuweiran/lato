package ardash.lato.actors;

import java.util.EnumSet;

public interface HasNextStates {
    EnumSet<PlayerState> nexts();
}
