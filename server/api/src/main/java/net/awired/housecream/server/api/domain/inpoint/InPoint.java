package net.awired.housecream.server.api.domain.inpoint;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.client.bean.validation.js.constraint.EnumNotEmpty;
import net.awired.housecream.server.api.domain.Point;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@NamedQueries({ @NamedQuery(name = InPoint.QUERY_BY_URL, //
query = "SELECT i FROM InPoint i where url = :" + InPoint.QUERY_PARAM_URL), //
        @NamedQuery(name = InPoint.QUERY_BY_ZONE, //
        query = "SELECT i FROM InPoint i where zoneId = :" + InPoint.QUERY_PARAM_ZONE_ID) })
public class InPoint extends Point {

    public static final String QUERY_BY_URL = "QUERY_BY_URL";
    public static final String QUERY_BY_ZONE = "QUERY_BY_ZONE";
    public static final String QUERY_PARAM_URL = "QUERY_PARAM_URL";
    public static final String QUERY_PARAM_ZONE_ID = "QUERY_PARAM_ZONE_ID";

    //    private Date lastUpdate; // input only

    private static final long serialVersionUID = 1L;

    @EnumNotEmpty
    private InPointType type;

    ////////////////////////////////////

    public void setType(InPointType type) {
        this.type = type;
    }

    public InPointType getType() {
        return type;
    }

}
