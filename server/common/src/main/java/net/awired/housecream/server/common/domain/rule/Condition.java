package net.awired.housecream.server.common.domain.rule;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Condition extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private Long pointId;
    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

}
