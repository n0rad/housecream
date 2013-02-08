package net.awired.housecream.server.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.awired.housecream.server.api.domain.PointState;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.drools.runtime.rule.FactHandle;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    private Map<Long, Pair<PointState, FactHandle>> states = Collections
            .synchronizedMap(new HashMap<Long, Pair<PointState, FactHandle>>());

    public Pair<PointState, FactHandle> updateAndGetPrevious(PointState state, FactHandle factHandler) {
        Pair<PointState, FactHandle> previous = states.put(state.getPointId(),
                new ImmutablePair<PointState, FactHandle>(state, factHandler));
        return previous;
    }

    public Pair<PointState, FactHandle> get(long pointId) {
        return states.get(pointId);
    }

    public void remove(long pointId) {
        states.remove(pointId);
    }

}
