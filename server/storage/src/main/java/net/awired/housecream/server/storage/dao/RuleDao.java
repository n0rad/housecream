package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.common.domain.rule.Rule;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao extends GenericDAOImpl<Rule, Long> {

    public RuleDao() {
        super(Rule.class, Long.class);
    }
}
