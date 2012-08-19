package net.awired.housecream.server.router;

import net.awired.housecream.server.common.domain.Point;
import net.awired.housecream.server.router.component.EndPointComponent;
import net.awired.housecream.server.router.component.RestMcuComponent;

public enum ComponentType {

    RESTMCU("restmcu", RestMcuComponent.class), //

    ;

    private final Class<? extends EndPointComponent> componentClass;
    private final String prefix;

    private ComponentType(String prefix, Class<? extends EndPointComponent> componentClass) {
        this.prefix = prefix;
        this.componentClass = componentClass;
    }

    public static EndPointComponent findComponentForPoint(Point point) {
        String urlPrefix = point.extractUrlPrefix();
        if (urlPrefix == null) {
            throw new RuntimeException("prefix not found in point url " + point);
        }
        for (ComponentType componentType : values()) {
            if (urlPrefix.equals(componentType.prefix)) {
                try {
                    return componentType.componentClass.newInstance();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        throw new RuntimeException("Component not found for prefix : " + urlPrefix);
    }
}
