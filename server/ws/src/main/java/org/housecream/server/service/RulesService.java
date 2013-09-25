/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.drools.definition.KnowledgePackage;
import org.housecream.server.api.domain.Order;
import org.housecream.server.api.domain.rule.Rule;
import org.housecream.server.api.resource.RulesResource;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.engine.builder.DroolsRuleBuilder;
import org.housecream.server.storage.dao.RuleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.client.bean.validation.js.service.ValidationService;

@Service
@Validated
public class RulesService implements RulesResource {

    @Autowired
    private RuleDao ruleDao;

    @Autowired
    private DroolsRuleBuilder ruleBuilder;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private ValidationService validationService;

    @Override
    public Rule createRule(Rule rule) {
        ruleDao.save(rule);
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        engine.registerPackages(build);
        return rule;
    }

    @Override
    public ClientValidatorInfo getRuleValidator() {
        return validationService.getValidatorInfo(Rule.class);
    }

    @PostConstruct
    public void postConstruct() {
        List<Rule> findAll = ruleDao.findAll();
        for (Rule rule : findAll) {
            Collection<KnowledgePackage> build = ruleBuilder.build(rule);
            engine.registerPackages(build);
        }
    }

    @Override
    public void deleteAllRules() {
        ruleDao.deleteAll();
    }

    @Override
    public List<Rule> getRules(Integer length, UUID start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<Rule> findAll = ruleDao.findAll();
        return findAll;
    }

}
