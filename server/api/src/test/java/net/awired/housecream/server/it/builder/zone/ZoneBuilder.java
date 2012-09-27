package net.awired.housecream.server.it.builder.zone;

import net.awired.housecream.server.api.domain.zone.Zone;

public abstract class ZoneBuilder<T extends ZoneBuilder<T>> {

    private Long id;
    private String name;
    private Long parentId;

    protected void build(Zone zone) {
        zone.setId(id);
        zone.setName(name);
        zone.setParentId(parentId);
    }

    //////////////////

    public T id(Long id) {
        this.id = id;
        return (T) this;
    }

    public T name(String name) {
        this.name = name;
        return (T) this;
    }

    public T parentId(Long parentId) {
        this.parentId = parentId;
        return (T) this;
    }

}
