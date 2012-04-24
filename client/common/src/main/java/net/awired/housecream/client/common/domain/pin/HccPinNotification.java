package net.awired.housecream.client.common.domain.pin;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pinNotification")
public class HccPinNotification {
    private int id;
    private float oldValue;
    private float value;
    private HccNotify notify;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public HccNotify getNotify() {
        return notify;
    }

    public void setNotify(HccNotify notify) {
        this.notify = notify;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
