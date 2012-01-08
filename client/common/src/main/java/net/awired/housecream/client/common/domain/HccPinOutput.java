package net.awired.housecream.client.common.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "outputPin")
@XmlAccessorType(XmlAccessType.FIELD)
public class HccPinOutput extends HccPin {

    private int startVal;

    public int getStartVal() {
        return startVal;
    }

    public void setStartVal(int startVal) {
        this.startVal = startVal;
    }

}
