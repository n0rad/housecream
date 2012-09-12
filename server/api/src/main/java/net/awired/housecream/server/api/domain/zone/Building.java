package net.awired.housecream.server.api.domain.zone;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.validator.ZoneParentType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@XmlRootElement(name = Building.ZONE_TYPE_NAME)
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonTypeName(Building.ZONE_TYPE_NAME)
public class Building extends Zone {

    public static final String ZONE_TYPE_NAME = "building";

    @Override
    @NotNull
    @ZoneParentType(daoName = "zoneDao", parentType = Land.class)
    public Long getParentId() {
        return super.getParentId();
    }

}
