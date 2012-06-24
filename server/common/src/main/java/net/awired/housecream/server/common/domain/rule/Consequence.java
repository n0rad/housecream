package net.awired.housecream.server.common.domain.rule;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Consequence extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private Long outPointId;
    private float value;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Long getOutPointId() {
        return outPointId;
    }

    public void setOutPointId(Long outPointId) {
        this.outPointId = outPointId;
    }
}
