package net.awired.housecream.server.common.domain.zone;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.google.common.base.Objects;

@XmlRootElement(name = "zones")
@XmlAccessorType(XmlAccessType.FIELD)
public class Zones {

    @XmlElement(name = "zone")
    private List<Zone> zones;
    private Long total;

    public Zones() {
    }

    public Zones(List<Zone> zones, Long total) {
        this.zones = zones;
        this.total = total;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("total", total) //
                .add("zones", zones) //
                .toString();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
}
