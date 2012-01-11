package net.awired.housecream.client.common.domain;

public class HccNotify {

    private HccCondition notifyCondition;
    private int notifyValue;

    public HccNotify() {
    }

    public HccNotify(HccCondition notifyCondition, int notifyValue) {
        this.notifyCondition = notifyCondition;
        this.notifyValue = notifyValue;
    }

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((notifyCondition == null) ? 0 : notifyCondition.hashCode());
        result = prime * result + notifyValue;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        HccNotify other = (HccNotify) obj;
        if (notifyCondition != other.notifyCondition) {
            return false;
        }
        if (notifyValue != other.notifyValue) {
            return false;
        }
        return true;
    }

}
