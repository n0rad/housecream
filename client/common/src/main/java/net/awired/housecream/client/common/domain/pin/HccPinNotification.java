package net.awired.housecream.client.common.domain.pin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pinNotification")
@XmlAccessorType(XmlAccessType.FIELD)
public class HccPinNotification {

    private int pinId;
    private float oldValue;
    private float value;
    private String source;
    private HccPinNotify notify;

    public float getOldValue() {
        return oldValue;
    }

    public void setOldValue(float oldValue) {
        this.oldValue = oldValue;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public HccPinNotify getNotify() {
        return notify;
    }

    public void setNotify(HccPinNotify notify) {
        this.notify = notify;
    }

    public void setPinId(int pinId) {
        this.pinId = pinId;
    }

    public int getPinId() {
        return pinId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
