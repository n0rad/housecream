package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.impl.NestedSetDaoImpl;
import net.awired.housecream.server.api.domain.zone.Zone;
import org.springframework.stereotype.Repository;

@Repository
public class ZoneDao extends NestedSetDaoImpl<Zone, Long> {

    public ZoneDao() {
        super(Zone.class, Long.class);
    }

}
