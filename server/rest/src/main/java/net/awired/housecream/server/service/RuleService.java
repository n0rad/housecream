package net.awired.housecream.server.service;

import java.util.Collection;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.resource.RuleResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.builder.RuleBuilder;
import net.awired.housecream.server.storage.dao.RuleDao;
import org.drools.definition.KnowledgePackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class RuleService implements RuleResource {

    @Inject
    private RuleDao ruleDao;

    @Inject
    private RuleBuilder ruleBuilder;

    @Inject
    private EngineProcessor engine;

    @Inject
    private ValidationService validationService;

    @Override
    public EventRule createRule(EventRule rule) {
        ruleDao.save(rule);
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        engine.registerPackages(build);
        return rule;
    }

    @Override
    public ClientValidatorInfo getRuleValidator() {
        return validationService.getValidatorInfo(EventRule.class);
    }

    @Override
    public void deleteRule(long ruleId) {
        ruleDao.delete(ruleId);
    }

    @Override
    public EventRule getRule(long ruleId) throws NotFoundException {
        return ruleDao.find(ruleId);
    }

}
