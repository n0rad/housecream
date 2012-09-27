package net.awired.housecream.server.api.domain.outPoint;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.domain.Point;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@NamedQueries({ @NamedQuery(name = OutPoint.QUERY_BY_ZONE, //
query = "SELECT i FROM OutPoint i where zoneId = :" + OutPoint.QUERY_PARAM_ZONE_ID) })
public class OutPoint extends Point {

    private static final long serialVersionUID = 1L;

    public static final String QUERY_PARAM_ZONE_ID = "QUERY_PARAM_ZONE_ID";
    public static final String QUERY_BY_ZONE = "QUERY_PARAM_ZONE_ID";

    private OutPointType type;

    public OutPointType getType() {
        return type;
    }

    public void setType(OutPointType type) {
        this.type = type;
    }

}
