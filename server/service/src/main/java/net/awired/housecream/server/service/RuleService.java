package net.awired.housecream.server.service;

import java.util.Collection;
import javax.inject.Inject;
import net.awired.housecream.server.common.domain.rule.EventRule;
import net.awired.housecream.server.common.resource.RuleResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.builder.RuleBuilder;
import net.awired.housecream.server.storage.dao.RuleDao;
import org.drools.definition.KnowledgePackage;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RuleService implements RuleResource {

    @Inject
    private RuleDao ruleDao;

    @Inject
    private RuleBuilder ruleBuilder;

    @Inject
    private EngineProcessor engine;

    @Override
    public EventRule createRule(EventRule rule) {
        ruleDao.save(rule);
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        engine.registerPackages(build);
        return rule; //TODO return id
    }

}
