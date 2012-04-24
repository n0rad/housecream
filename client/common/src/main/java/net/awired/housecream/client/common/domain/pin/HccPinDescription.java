package net.awired.housecream.client.common.domain.pin;

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
    private Float valueMin;
    private Float valueMax;
    private Float valueStep; // output only

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

    public void setTechnicalDescription(String technicalDescription) {
        this.technicalDescription = technicalDescription;
    }

    public String getTechnicalDescription() {
        return technicalDescription;
    }

    public void setValueStep(Float valueStep) {
        this.valueStep = valueStep;
    }

    public Float getValueStep() {
        return valueStep;
    }

}
