package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.rule.EventRule;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao extends GenericDaoImpl<EventRule, Long> {

    public RuleDao() {
        super(EventRule.class, Long.class);
    }
}
