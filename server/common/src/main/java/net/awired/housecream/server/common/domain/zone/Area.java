package net.awired.housecream.server.common.domain.zone;

import java.util.List;
import net.awired.ajsl.persistence.entity.IdEntityImpl;
import net.awired.housecream.server.common.domain.Coordinate;
import net.awired.housecream.server.common.domain.Point;

public class Area extends IdEntityImpl<Long> {

    private String name;

    private List<Coordinate> location;

    private List<Point> points;
}
