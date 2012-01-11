package net.awired.housecream.client.common.domain;

public class HccPin {

    private HccPinInfo info;

    private Integer value;

    private HccPinDescription description;

    public HccPinInfo getInfo() {
        return info;
    }

    public void setInfo(HccPinInfo info) {
        this.info = info;
    }

    public HccPinDescription getDescription() {
        return description;
    }

    public void setDescription(HccPinDescription description) {
        this.description = description;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
