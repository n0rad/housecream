package net.awired.housecream.client.common.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pinInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class HccPinInfo {

    private String name;

    private String description;

    private Float startVal; // output only
    private List<HccNotify> notifies; // input only

    public void addNotify(HccNotify notify) {
        if (this.notifies == null) {
            this.notifies = new ArrayList<HccNotify>();
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

    public List<HccNotify> getNotifies() {
        return notifies;
    }

    public void setNotifies(List<HccNotify> notifies) {
        this.notifies = notifies;
    }

    public Float getStartVal() {
        return startVal;
    }

    public void setStartVal(Float startVal) {
        this.startVal = startVal;
    }
}
