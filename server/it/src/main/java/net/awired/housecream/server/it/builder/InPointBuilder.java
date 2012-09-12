package net.awired.housecream.server.it.builder;

import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;

public class InPointBuilder {

    private String name;
    private InPointType type;
    private String url;
    private long zoneId;

    public InPoint build() {
        InPoint inPoint = new InPoint();
        inPoint.setName(name);
        inPoint.setType(type);
        inPoint.setUrl(url);
        inPoint.setZoneId(zoneId);
        return inPoint;
    }

    public InPointBuilder zoneId(long zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public InPointBuilder name(String name) {
        this.name = name;
        return this;
    }

    public InPointBuilder type(InPointType type) {
        this.type = type;
        return this;
    }

    public InPointBuilder url(String url) {
        this.url = url;
        return this;
    }
}
