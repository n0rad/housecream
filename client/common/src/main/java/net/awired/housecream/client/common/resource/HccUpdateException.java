package net.awired.housecream.client.common.resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hccError")
@XmlAccessorType(XmlAccessType.NONE)
public class HccUpdateException extends Exception {

    private static final long serialVersionUID = 1L;

    public HccUpdateException() {
    }

    public HccUpdateException(String message) {
        super(message);
    }

    @XmlElement
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
