package net.awired.housecream.server.api.domain.rule;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import com.google.common.base.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Consequence extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private long outPointId;
    private float value;
    private long delayMili;
    private TriggerType triggerType;

    public Consequence() {
    }

    public Consequence(long outPointId, float value) {
        this.outPointId = outPointId;
        this.value = value;
    }

    public Consequence(long outPointId, float value, long delayMili) {
        this.outPointId = outPointId;
        this.value = value;
        this.delayMili = delayMili;
    }

    public Consequence(long outPointId, float value, long delayMili, TriggerType trigger) {
        this.outPointId = outPointId;
        this.value = value;
        this.delayMili = delayMili;
        this.triggerType = trigger;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("pointId", outPointId) //
                .add("value", value) //
                .add("delayMili", delayMili) //
                .add("triggerType", triggerType) //
                .toString();
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

    public long getDelayMili() {
        return delayMili;
    }

    public void setDelayMili(long delayMili) {
        this.delayMili = delayMili;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }
}
