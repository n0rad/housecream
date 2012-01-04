package net.awired.housecream.server.storage.entity;

import net.awired.ajsl.persistence.entity.IdEntityImpl;

public class PointType extends IdEntityImpl<Long> {

    private static final long serialVersionUID = 1L;

    private String typeName; // light, switch, variator
    // private ValueType values; // range

}
