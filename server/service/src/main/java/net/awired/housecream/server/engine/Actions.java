package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.List;

public class Actions {

    private final List<ConsequenceAction> actions = new ArrayList<ConsequenceAction>();

    public void add(ConsequenceAction action) {
        actions.add(action);
    }

    public List<ConsequenceAction> getActions() {
        return actions;
    }

}
