/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.drools.runtime.rule.FactHandle;
import org.housecream.server.api.domain.PointState;
import org.housecream.server.service.EventWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    @Autowired
    private EventWebSocketService webSocketService;

    private Map<UUID, Pair<PointState, FactHandle>> states = Collections
            .synchronizedMap(new HashMap<UUID, Pair<PointState, FactHandle>>());

    public Pair<PointState, FactHandle> updateAndGetPrevious(PointState state, FactHandle factHandler) {
        Pair<PointState, FactHandle> previous = states.put(state.getPointId(),
                new ImmutablePair<>(state, factHandler));
        webSocketService.notifyStateUpdate(state);
        return previous;
    }

    public Pair<PointState, FactHandle> get(UUID pointId) {
        return states.get(pointId);
    }

    public void remove(UUID pointId) {
        states.remove(pointId);
    }

}
