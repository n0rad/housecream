package net.awired.housecream.server.storage.entity;

import javax.persistence.Entity;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

/**
 * A capture or command point (temperature, interruptor, ...)
 */
@Entity
public class EndPoint extends IdEntityImpl<Long> {

    //    private Coordinate position;
    private Device device;

    private String name;

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
