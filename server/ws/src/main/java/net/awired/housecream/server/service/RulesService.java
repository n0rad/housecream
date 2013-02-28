package net.awired.housecream.server.service;

import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.rule.Rules;
import net.awired.housecream.server.api.resource.RulesResource;
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
public class RulesService implements RulesResource {

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

    @PostConstruct
    public void postConstruct() {
        List<EventRule> findAll = ruleDao.findAll();
        for (EventRule rule : findAll) {
            Collection<KnowledgePackage> build = ruleBuilder.build(rule);
            engine.registerPackages(build);
        }
    }

    @Override
    public void deleteAllRules() {
        ruleDao.deleteAll();
    }

    @Override
    public Rules getRules(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<EventRule> findAll = ruleDao.findAll();
        Rules zones = new Rules();
        zones.setRules(findAll);
        zones.setTotal((long) findAll.size());
        return zones;
    }

}
