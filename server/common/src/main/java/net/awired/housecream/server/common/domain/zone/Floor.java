package net.awired.housecream.server.common.domain.zone;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@Entity
@XmlRootElement(name = "floor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Floor extends IdEntityImpl<Long> {

    private String name;
    private Integer ordering;
    private byte[] image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    @Override
    @XmlElement
    public Long getId() {
        return super.getId();
    }

}
