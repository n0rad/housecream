/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.service;

import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.Order;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.rule.Rules;
import net.awired.housecream.server.api.resource.RulesResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.builder.RuleBuilder;
import net.awired.housecream.server.storage.dao.RuleDao;
import org.drools.definition.KnowledgePackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class RulesService implements RulesResource {

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private RuleBuilder ruleBuilder;

    @Autowired
    private EngineProcessor engine;

    @Autowired
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
