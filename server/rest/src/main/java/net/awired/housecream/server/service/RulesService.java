package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.housecream.server.api.resource.RulesResource;
import net.awired.housecream.server.storage.dao.RuleDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class RulesService implements RulesResource {

    @Inject
    private RuleDao ruleDao;

    @Override
    public void deleteAllRules() {
        ruleDao.deleteAll();
    }
}
