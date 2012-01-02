package net.awired.housecream.server.storage.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

/**
 * A capture or command point (temperature, switch, ...)
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class EndPoint extends IdEntityImpl<Long> {

    //    private Coordinate position;

    //  @ManyToOne
    //   private Device device;

    private String name;

    //    public void setDevice(Device device) {
    //        this.device = device;
    //    }

    //    public Device getDevice() {
    //        return device;
    //    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    @XmlElement
    public Long getId() {
        return super.getId();
    }

}
