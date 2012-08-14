package net.awired.housecream.server.common.domain.zone;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@XmlRootElement(name = Floor.ZONE_TYPE_NAME)
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonTypeName(Floor.ZONE_TYPE_NAME)
public class Floor extends Zone {

    public static final String ZONE_TYPE_NAME = "floor";

    //    private long buildingId;
    //
    //    public long getBuildingId() {
    //        return buildingId;
    //    }
    //
    //    public void setBuildingId(long buildingId) {
    //        this.buildingId = buildingId;
    //    }

}
