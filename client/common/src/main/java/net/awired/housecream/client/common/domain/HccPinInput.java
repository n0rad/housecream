package net.awired.housecream.client.common.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "inputPin")
@XmlAccessorType(XmlAccessType.FIELD)
public class HccPinInput extends HccPin {

    private HccCondition notifyCondition;
    private int notifyValue;
    private boolean pullupResistor;

    public HccCondition getNotifyCondition() {
        return notifyCondition;
    }

    public void setNotifyCondition(HccCondition notifyCondition) {
        this.notifyCondition = notifyCondition;
    }

    public int getNotifyValue() {
        return notifyValue;
    }

    public void setNotifyValue(int notifyValue) {
        this.notifyValue = notifyValue;
    }

    public boolean isPullupResistor() {
        return pullupResistor;
    }

    public void setPullupResistor(boolean pullupResistor) {
        this.pullupResistor = pullupResistor;
    }

}
