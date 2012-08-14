package net.awired.housecream.server.common.domain.zone;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@XmlRootElement(name = Area.ZONE_TYPE_NAME)
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonTypeName(Area.ZONE_TYPE_NAME)
public class Area extends Zone {

    public static final String ZONE_TYPE_NAME = "area";

    //    private Long roomId;
    //
    //    public Long getRoomId() {
    //        return roomId;
    //    }
    //
    //    public void setRoomId(Long roomId) {
    //        this.roomId = roomId;
    //    }

}
