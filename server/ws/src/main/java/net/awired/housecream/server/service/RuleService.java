package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.resource.RuleResource;
import net.awired.housecream.server.storage.dao.RuleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class RuleService implements RuleResource {

    @Inject
    private RuleDao ruleDao;

    @Override
    public void deleteRule(long ruleId) {
        ruleDao.delete(ruleId);
    }

    @Override
    public EventRule getRule(long ruleId) throws NotFoundException {
        return ruleDao.find(ruleId);
    }
}
