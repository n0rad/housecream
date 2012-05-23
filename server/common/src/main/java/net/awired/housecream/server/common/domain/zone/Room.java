package net.awired.housecream.server.common.domain.zone;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import net.awired.housecream.server.common.domain.Coordinate;

@Entity
public class Room extends Area {

    private String name;
    private Floor floor;

    @ElementCollection
    private List<Coordinate> coverage;

}
