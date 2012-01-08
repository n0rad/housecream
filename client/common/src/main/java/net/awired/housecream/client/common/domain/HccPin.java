package net.awired.housecream.client.common.domain;

public class HccPin {

    private String name; //
    private String description; //
    private HccPinDirection direction;
    private HccPinType type;

    //    private int rangeMin;
    //    private int rangeMax;
    //    private int rangeStep;

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

    public HccPinDirection getDirection() {
        return direction;
    }

    public void setDirection(HccPinDirection direction) {
        this.direction = direction;
    }

    public HccPinType getType() {
        return type;
    }

    public void setType(HccPinType type) {
        this.type = type;
    }

}
