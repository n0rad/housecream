package net.awired.housecream.server.api.domain.outPoint;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "outPoints")
@XmlAccessorType(XmlAccessType.FIELD)
public class OutPoints {

    @XmlElement(name = "outPoint")
    private List<OutPoint> outPoints;
    private Long total;

    public OutPoints() {
    }

    public OutPoints(List<OutPoint> outPoints, Long total) {
        this.outPoints = outPoints;
        this.total = total;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("total", total) //
                .add("inPoints", outPoints) //
                .toString();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<OutPoint> getOutPoints() {
        return outPoints;
    }

    public void setOutPoints(List<OutPoint> outPoints) {
        this.outPoints = outPoints;
    }

}
