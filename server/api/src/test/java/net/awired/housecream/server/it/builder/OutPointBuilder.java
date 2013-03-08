package net.awired.housecream.server.it.builder;

import java.net.URI;
import java.net.URISyntaxException;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;

public class OutPointBuilder {

    private URI uri;
    private OutPointType type;
    private String name;
    private long zoneId;

    public static OutPointBuilder out() {
        return new OutPointBuilder();
    }

    public OutPoint build() {
        OutPoint outPoint = new OutPoint();
        outPoint.setName(name);
        outPoint.setType(type);
        outPoint.setUri(uri);
        outPoint.setZoneId(zoneId);
        return outPoint;
    }

    public OutPointBuilder uri(String stringUri) {
        try {
            this.uri = new URI(stringUri);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public OutPointBuilder uri(URI uri) {
        this.uri = uri;
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
