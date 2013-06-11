package net.awired.housecream.server.api.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * This is the class representing a client (arduino board, crumb644-net, ...).
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Device {

    private String url = "hcc:http://192.168.42.4";

    //    private DeviceType type;

    //    private Coordinate position;

}
