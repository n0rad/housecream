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
package net.awired.housecream.server.engine.builder;

import static net.awired.core.lang.Caster.cast;
import static net.awired.housecream.server.api.domain.rule.TriggerType.NON_RETRIGGER;
import static org.fest.assertions.api.Assertions.assertThat;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import net.awired.core.lang.Caster;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.rule.TriggerType;
import net.awired.housecream.server.engine.Action;
import net.awired.housecream.server.engine.Actions;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.drools.definition.KnowledgePackage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RuleBuilderTest {

    @Mock
    private StateService stateService;

    @InjectMocks
    private RuleBuilder ruleBuilder = new RuleBuilder();

    @InjectMocks
    private EngineProcessor engine;

    @Before
    public void before() throws Exception {
        engine = new EngineProcessor();
        ReflectionTestUtils.setField(engine, "ruleBuilder", ruleBuilder);
        ReflectionTestUtils.setField(engine, "stateService", stateService);
        Method m = engine.getClass().getDeclaredMethod("postConstruct", (Class<?>[]) null);
        m.setAccessible(true);
        m.invoke(engine, null);
    }

    @Test
    public void should_change_state_for_simple_rule() throws Exception {

        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(44, 1, ConditionType.event));
        rule.getConsequences().add(new Consequence(45, 1));
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);

        engine.registerPackages(build);

        Exchange exchange = new DefaultExchange((CamelContext) null);
        Message in = new DefaultMessage();
        Event e = new Event();
        e.setPointId(44);
        e.setValue(1f);
        in.setBody(e);
        exchange.setIn(in);

        engine.process(exchange);

        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getOutPointId()).isEqualTo(45);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(1f);
    }

    @Test
    public void should_toggle_from_pushbutton_starting_with_on_if_no_state() throws Exception {
        // rule
        EventRule rule = new EventRule();
        rule.setSalience(100);
        rule.setName("set 43 to 1 when 43=0 or !42");
        rule.getConditions().add(new Condition(42, 1, ConditionType.event));
        rule.getConditions().add(new Condition(43, 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(43, 1));
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        // rule 2
        EventRule rule2 = new EventRule();
        rule2.setName("set 43 to 0 when 43=1");
        rule2.getConditions().add(new Condition(42, 1, ConditionType.event));
        rule2.getConditions().add(new Condition(43, 1, ConditionType.state));
        rule2.getConsequences().add(new Consequence(43, 0));
        Collection<KnowledgePackage> build2 = ruleBuilder.build(rule2);
        engine.registerPackages(build);
        engine.registerPackages(build2);

        Exchange exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(1);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getOutPointId()).isEqualTo(43);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(1f);

        engine.findAndRemoveActionFromFacts(exchange.getIn().getBody(Actions.class).getActions().get(0)); // remove action
        engine.setPointState(43, 1f); // save new state

        exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(1);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getOutPointId()).isEqualTo(43);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(0f);
    }

    @Test
    public void should_handle_non_retriggerable() throws Exception {
        // rule
        EventRule rule = new EventRule();
        rule.setName("non retrigger 43 on push on 42");
        rule.getConditions().add(new Condition(42, 1, ConditionType.event));
        rule.getConditions().add(new Condition(43, 0, ConditionType.state));
        Consequence direct1 = new Consequence(43, 1);
        Consequence delayed0 = new Consequence(43, 0, 1000, NON_RETRIGGER);
        rule.getConsequences().add(delayed0);
        rule.getConsequences().add(direct1);
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        engine.registerPackages(build);

        Exchange exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(2);

        assertThat(cast(exchange.getIn().getBody(Actions.class).getActions(), Consequence.class)).containsOnly(
                delayed0, direct1);

        engine.setPointState(43, 1f);

        exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(0);
    }

    @Test
    public void should_handle_retrigger_before_end_of_delay() throws Exception {
        // rule
        EventRule rule = new EventRule();
        rule.setName("Retrigger 43 on push on 42");
        rule.getConditions().add(new Condition(42, 1, ConditionType.event));
        Consequence direct1 = new Consequence(43, 1);
        Consequence delayed0 = new Consequence(43, 0, 1000, TriggerType.RETRIGGER);
        rule.getConsequences().add(direct1);
        rule.getConsequences().add(delayed0);
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        engine.registerPackages(build);

        Exchange exchange = buildExchange(1f);
        engine.process(exchange);
        List<Action> actions = exchange.getIn().getBody(Actions.class).getActions();
        assertThat(actions).hasSize(2);
        assertThat(Caster.cast(actions, Consequence.class)).contains(delayed0, direct1);
        Action currentDelayAction = actions.get(actions.indexOf(delayed0));
        engine.setPointState(43, 1f);

        exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(actions).hasSize(2);
        assertThat(cast(actions, Consequence.class)).contains(delayed0, direct1);
        assertThat(engine.findAndRemoveActionFromFacts(currentDelayAction)).isFalse();
    }

    private Exchange buildExchange(float value) {
        Exchange exchange = new DefaultExchange((CamelContext) null);
        Message in = new DefaultMessage();
        Event e = new Event();
        e.setPointId(42);
        e.setValue(value);
        in.setBody(e);
        exchange.setIn(in);
        return exchange;
    }

}
