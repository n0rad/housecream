package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Objects;

public class Actions {

    private final List<Action> actions = new ArrayList<Action>();

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("actions", actions) //
                .toString();
    }

    public void add(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }

}
