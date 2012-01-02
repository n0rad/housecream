package net.awired.housecream.server.storage.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Device extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private String description = "interrupteur 42";
    private String url = "httparduino:http://192.168.42.4/4";

    //    private DeviceType type;
    //    private DeviceWay way;
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
