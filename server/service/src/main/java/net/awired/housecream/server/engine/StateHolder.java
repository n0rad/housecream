package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.PointState;
import org.springframework.stereotype.Component;

@Component
public class StateHolder {

    private Map<Long, Float> states = Collections.synchronizedMap(new HashMap<Long, Float>());

    //TODO should be point instead of id
    public Float getState(long pointId) throws NotFoundException {
        //        if (!states.containsKey(pointId)) {
        //            throw new NotFoundException("No state for point with Id " + pointId);
        //        }
        return states.get(pointId);
    }

    public void setState(long pointId, Float currentValue) {
        states.put(pointId, currentValue);
    }

    public List<Object> getFacts() {
        List<Object> arrayList = new ArrayList<Object>();
        for (Long key : states.keySet()) {
            Float value = states.get(key);
            if (value != null) {
                arrayList.add(new PointState(key, value));
            }
        }
        return arrayList;
    }

    public void removeState(long pointId) {
        states.remove(pointId);
    }

}
