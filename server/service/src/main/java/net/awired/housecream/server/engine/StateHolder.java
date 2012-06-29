package net.awired.housecream.server.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.awired.ajsl.core.lang.exception.NotFoundException;
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

    public void setState(long pointId, Float currentValue) {
        states.put(pointId, currentValue);
    }

    public List<Object> getFacts() {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        for (Long key : states.keySet()) {
            arrayList.add(new PointStat(key, states.get(key)));
        }
        return arrayList;
    }

}
