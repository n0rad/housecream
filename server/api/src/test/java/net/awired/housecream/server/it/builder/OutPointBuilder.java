package net.awired.housecream.server.it.builder;

import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;

public class OutPointBuilder {

    private String url;
    private OutPointType type;
    private String name;
    private long zoneId;

    public OutPoint build() {
        OutPoint outPoint = new OutPoint();
        outPoint.setName(name);
        outPoint.setType(type);
        outPoint.setUrl(url);
        outPoint.setZoneId(zoneId);
        return outPoint;
    }

    public OutPointBuilder url(String url) {
        this.url = url;
        return this;
    }

    public OutPointBuilder type(OutPointType type) {
        this.type = type;
        return this;
    }

    public OutPointBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OutPointBuilder zoneId(long zoneId) {
        this.zoneId = zoneId;
        return this;
    }

}
