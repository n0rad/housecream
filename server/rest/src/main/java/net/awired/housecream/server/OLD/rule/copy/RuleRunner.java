/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.awired.housecream.server.OLD.rule.copy;

import java.util.Collection;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class RuleRunner {

    public RuleRunner() {
    }

    public void runRules(String[] rules, Object[] facts) {

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        for (String ruleFile : rules) {
            System.out.println("Loading file: " + ruleFile);
            kbuilder.add(ResourceFactory.newClassPathResource(ruleFile, RuleRunner.class), ResourceType.DRL);
        }

        Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
        kbase.addKnowledgePackages(pkgs);
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

        for (Object fact : facts) {
            System.out.println("Inserting fact: " + fact);
            ksession.insert(fact);
        }

        ksession.fireAllRules();

        for (Object fact : facts) {
            System.out.println("Inserting fact: " + fact);
            ksession.insert(fact);
        }

        System.out.println("sss");
        long currentTimeMillis = System.currentTimeMillis();
        ksession.fireAllRules();
        System.out.println(System.currentTimeMillis() - currentTimeMillis);

    }
}
