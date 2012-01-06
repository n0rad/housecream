package net.awired.housecream.server.common.domain;

import java.sql.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A capture or command point (temperature, switch, ...)
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Point {

    private static final long serialVersionUID = 1L;

    private Long id;

    //  private Coordinate position;
    private String name = "Interrupteur 42";
    private String description = "Eclairage principal de la piece";
    private String url = "httparduino:http://192.168.42.4/4";
    private Integer devicePointId = 4;
    //   private PointWay way;
    private Date lastUpdate;

    //    private Room room;
    private Device device;

    ////////////////////

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
