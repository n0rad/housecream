package net.awired.housecream.server.core.engine;

import java.util.HashMap;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.common.domain.Point;
import org.springframework.stereotype.Component;

@Component
public class StateHolder {

    private HashMap<Long, Float> states = new HashMap<Long, Float>();

    //TODO should be point instead of id
    public Float getState(long pointId) throws NotFoundException {
        if (!states.containsKey(pointId)) {
            throw new NotFoundException("No state for point with Id " + pointId);
        }
        return states.get(pointId);
    }

    public void setState(Point point, Float currentValue) {
        states.put(point.getId(), currentValue);
    }

}
