package net.awired.housecream.server.api.domain.inpoint;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "inPoints")
@XmlAccessorType(XmlAccessType.FIELD)
public class InPoints {

    @XmlElement(name = "inPoint")
    private List<InPoint> inPoints;
    private Long total;

    public InPoints() {
    }

    public InPoints(List<InPoint> inPoints, Long total) {
        this.inPoints = inPoints;
        this.total = total;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("total", total) //
                .add("inPoints", inPoints) //
                .toString();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<InPoint> getInPoints() {
        return inPoints;
    }

    public void setInPoints(List<InPoint> inPoints) {
        this.inPoints = inPoints;
    }

}
