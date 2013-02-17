package net.awired.housecream.server.storage.dao;

import java.util.List;
import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import org.springframework.stereotype.Repository;

@Repository
public class RuleDao extends GenericDaoImpl<EventRule, Long> {

    public RuleDao() {
        super(EventRule.class, Long.class);
    }

    @Override
    public List<EventRule> findAll() {
        List<EventRule> findList = super.findAll();
        for (EventRule eventRule : findList) {
            for (@SuppressWarnings("unused")
            Condition condition : eventRule.getConditions()) {
            }
            for (@SuppressWarnings("unused")
            Consequence consequence : eventRule.getConsequences()) {
            }
        }
        return findList;
    }
}
