package net.awired.housecream.server.storage.entity;

import javax.persistence.Entity;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.ajsl.persistence.entity.OrderedEntity;

@Entity
public class Floor extends IdEntityImpl<Long> implements OrderedEntity {

    private String name;
    private Integer ordering;
    private byte[] image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getOrdering() {
        return ordering;
    }

    @Override
    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }
}
