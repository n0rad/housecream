package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.server.api.domain.rule.Consequence;
import com.google.common.base.Objects;

public class Actions {

    private final List<Consequence> actions = new ArrayList<Consequence>();

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("actions", actions) //
                .toString();
    }

    public void add(Consequence action) {
        actions.add(action);
    }

    public List<Consequence> getActions() {
        return actions;
    }

}
