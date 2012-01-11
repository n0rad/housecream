package net.awired.housecream.client.common.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pin")
@XmlAccessorType(XmlAccessType.FIELD)
public class HccPinDescription {

    private String technicalDescription;
    private HccPinDirection direction;
    private HccPinType type;
    private Boolean pullUp; // if not reserved/notused
    private Integer valueMin; // analog only
    private Integer valueMax; // analog only

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

    public void setPullUp(Boolean pullUp) {
        this.pullUp = pullUp;
    }

    public Boolean getPullUp() {
        return pullUp;
    }

    public void setValueMin(Integer valueMin) {
        this.valueMin = valueMin;
    }

    public Integer getValueMin() {
        return valueMin;
    }

    public void setValueMax(Integer valueMax) {
        this.valueMax = valueMax;
    }

    public Integer getValueMax() {
        return valueMax;
    }

    public void setTechnicalDescription(String technicalDescription) {
        this.technicalDescription = technicalDescription;
    }

    public String getTechnicalDescription() {
        return technicalDescription;
    }

}
