package net.awired.housecream.server.common.domain.zone;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@XmlRootElement(name = Building.ZONE_TYPE_NAME)
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonTypeName(Building.ZONE_TYPE_NAME)
public class Building extends Zone {

    public static final String ZONE_TYPE_NAME = "building";

    //    public long getLandId() {
    //        return parentId;
    //    }

}
