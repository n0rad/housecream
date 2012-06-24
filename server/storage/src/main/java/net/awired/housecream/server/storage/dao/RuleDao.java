package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.common.domain.rule.EventRule;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao extends GenericDAOImpl<EventRule, Long> {

    public RuleDao() {
        super(EventRule.class, Long.class);
    }
}
