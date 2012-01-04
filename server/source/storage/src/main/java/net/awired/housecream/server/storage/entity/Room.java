package net.awired.housecream.server.storage.entity;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@Entity
public class Room extends IdEntityImpl<Long> {

    private String name;
    private Floor floor;

    @ElementCollection
    private List<Coordinate> coverage;

}
