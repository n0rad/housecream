package net.awired.housecream.server.common.domain.zone;

import java.util.List;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

public class Land extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private List<Building> building;

    public void setBuilding(List<Building> building) {
        this.building = building;
    }

    public List<Building> getBuilding() {
        return building;
    }
}
