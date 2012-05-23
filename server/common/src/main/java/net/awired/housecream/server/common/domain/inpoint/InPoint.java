package net.awired.housecream.server.common.domain.inpoint;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.common.domain.Point;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
public class InPoint extends Point {

    //    private Date lastUpdate; // input only

    private static final long serialVersionUID = 1L;

    private InPointType type;

    ////////////////////////////////////

    public void setType(InPointType type) {
        this.type = type;
    }

    public InPointType getType() {
        return type;
    }

}
