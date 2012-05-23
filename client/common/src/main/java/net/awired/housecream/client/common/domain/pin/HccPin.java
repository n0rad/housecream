package net.awired.housecream.client.common.domain.pin;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pin")
@XmlAccessorType(XmlAccessType.FIELD)
public class HccPin {

    private String name; // updatable
    private String description;
    private HccPinDirection direction;
    private HccPinType type;
    private Float valueMin;
    private Float valueMax;

    private List<HccPinNotify> notifies; // input only, updatable

    public void addNotify(HccPinNotify notify) {
        if (this.notifies == null) {
            this.notifies = new ArrayList<HccPinNotify>();
        }
        this.notifies.add(notify);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<HccPinNotify> getNotifies() {
        return notifies;
    }

    public void setNotifies(List<HccPinNotify> notifies) {
        this.notifies = notifies;
    }

    public HccPinDirection getDirection() {
        return direction;
    }

    public HccPinType getType() {
        return type;
    }

    public void setType(HccPinType type) {
        this.type = type;
    }

    public void setDirection(HccPinDirection direction) {
        this.direction = direction;
    }

    public void setValueMin(Float valueMin) {
        this.valueMin = valueMin;
    }

    public Float getValueMin() {
        return valueMin;
    }

    public void setValueMax(Float valueMax) {
        this.valueMax = valueMax;
    }

    public Float getValueMax() {
        return valueMax;
    }

}
