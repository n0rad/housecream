package net.awired.housecream.server.common.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the class representing a client (arduino board, crumb644-net, ...).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Device {

    private String url = "hcc:http://192.168.42.4";

    //    private DeviceType type;

    //    private Coordinate position;

    ///////////////////////////////////////////////////////////

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
