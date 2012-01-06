package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.common.domain.Floor;
import org.springframework.stereotype.Repository;

@Repository
public class FloorDao extends GenericDAOImpl<Floor, Long> {

    public FloorDao() {
        super(Floor.class, Long.class);
    }

}
