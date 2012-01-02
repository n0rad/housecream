package net.awired.housecream.server.storage.entity;

import javax.persistence.Entity;
import net.awired.ajsl.persistence.entity.IdEntityImpl;

@Entity
public class Room extends IdEntityImpl<Long> {

    private String name;
    private Floor floor;

    //    private List<Coordinate> coverage;

}
