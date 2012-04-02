package net.awired.housecream.client.common.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pinNotify")
public class HccPinNotify {

    private int pinId;
    private String address; // do not trust this value, check remoteAddress
    private float value;
    private HccNotify condition;

    public int getPinId() {
        return pinId;
    }

    public void setPinId(int pinId) {
        this.pinId = pinId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public HccNotify getCondition() {
        return condition;
    }

    public void setCondition(HccNotify condition) {
        this.condition = condition;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
