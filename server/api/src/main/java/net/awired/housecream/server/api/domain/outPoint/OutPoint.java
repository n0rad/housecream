package net.awired.housecream.server.api.domain.outPoint;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.housecream.server.api.domain.Point;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
public class OutPoint extends Point {

    private static final long serialVersionUID = 1L;

    private OutPointType type;

    public OutPointType getType() {
        return type;
    }

    public void setType(OutPointType type) {
        this.type = type;
    }

}
