package net.awired.housecream.server.api.domain.zone;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.validator.ZoneParentType;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@XmlRootElement(name = Field.ZONE_TYPE_NAME)
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonTypeName(Field.ZONE_TYPE_NAME)
public class Field extends Zone {

    public static final String ZONE_TYPE_NAME = "field";

    @Override
    @NotNull
    @ZoneParentType(daoName = "zoneDao", parentType = Land.class)
    public Long getParentId() {
        return super.getParentId();
    }

}
