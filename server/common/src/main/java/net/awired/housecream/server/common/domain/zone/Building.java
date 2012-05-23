package net.awired.housecream.server.common.domain.zone;

import java.util.List;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.housecream.server.common.domain.Coordinate;

public class Building extends IdEntityImpl<Long> {

    List<Coordinate> location;

    List<Floor> floors;

}
