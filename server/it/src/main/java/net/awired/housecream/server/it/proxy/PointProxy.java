package net.awired.housecream.server.it.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.server.common.domain.Point;

public class PointProxy<T> extends ProxyClass<T> {

    private final List<Long> points = new ArrayList<Long>();

    public PointProxy(T o) {
        super(o);
    }

    @Override
    protected void handleBefore(Method m, Object[] args) {
    }

    @Override
    protected void handleAfter(Method m, Object[] args) {
    }

    @Override
    protected void handleSuccess(Method m, Object[] args, Object result) {
        if (m.getName().equals("createPoint")) {
            points.add(((Point) result).getId());
        }
    }

    public List<Long> getPoints() {
        return points;
    }

}
