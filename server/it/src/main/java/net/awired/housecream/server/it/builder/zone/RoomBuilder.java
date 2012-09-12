package net.awired.housecream.server.it.builder.zone;

import net.awired.housecream.server.api.domain.zone.Room;

public class RoomBuilder extends ZoneBuilder<RoomBuilder> {

    public Room build() {
        Room room = new Room();
        build(room);
        return room;
    }

}
