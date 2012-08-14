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

    private long outPointId;
    private float value;

    public Consequence() {
    }

    public Consequence(long outPointId, float value) {
        this.outPointId = outPointId;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public long getOutPointId() {
        return outPointId;
    }

    public void setOutPointId(long outPointId) {
        this.outPointId = outPointId;
    }
}
