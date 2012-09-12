package net.awired.housecream.server.api.domain.zone;

import javax.persistence.Entity;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@XmlRootElement(name = Land.ZONE_TYPE_NAME)
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonTypeName(Land.ZONE_TYPE_NAME)
public class Land extends Zone {

    public static final String ZONE_TYPE_NAME = "land";

    @Override
    @Null
    public Long getParentId() {
        return super.getParentId();
    }
}
