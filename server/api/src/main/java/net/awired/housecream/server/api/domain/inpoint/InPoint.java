package net.awired.housecream.server.api.domain.inpoint;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.domain.Point;

@XmlRootElement(name = "InPoint")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@NamedQueries({ @NamedQuery(name = InPoint.QUERY_BY_ZONE, //
query = "SELECT i FROM InPoint i where zoneId = :" + InPoint.QUERY_PARAM_ZONE_ID) })
public class InPoint extends Point {

    public static final String QUERY_BY_ZONE = "QUERY_BY_ZONE";
    public static final String QUERY_PARAM_ZONE_ID = "QUERY_PARAM_ZONE_ID";

    //    private Date lastUpdate; // input only

    private static final long serialVersionUID = 1L;

    //    @EnumNotEmpty
    //    @ApiProperty(value = "type of point", allowableValues = "any")
    private InPointType type;

    ////////////////////////////////////

    public void setType(InPointType type) {
        this.type = type;
    }

    //    @ApiProperty(value = "type of point", allowableValues = "any")
    @XmlElement
    public InPointType getType() {
        return type;
    }

}
