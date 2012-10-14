package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Objects;

public class Actions {

    private final List<ConsequenceAction> actions = new ArrayList<ConsequenceAction>();

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("actions", actions) //
                .toString();
    }

    public void add(ConsequenceAction action) {
        actions.add(action);
    }

    public List<ConsequenceAction> getActions() {
        return actions;
    }

}
