package net.awired.housecream.server.engine.builder;

import static net.awired.housecream.server.api.domain.rule.TriggerType.NON_RETRIGGER;
import static org.fest.assertions.Assertions.assertThat;
import java.lang.reflect.Method;
import java.util.Collection;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.rule.TriggerType;
import net.awired.housecream.server.engine.Actions;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.service.event.EventService;
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

@RunWith(MockitoJUnitRunner.class)
public class RuleBuilderTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private RuleBuilder ruleBuilder = new RuleBuilder();

    @InjectMocks
    private EngineProcessor engine = new EngineProcessor();

    @Before
    public void before() throws Exception {
        engine = new EngineProcessor();
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

        engine.removeConsequenceFromState(exchange.getIn().getBody(Actions.class).getActions().get(0)); // remove action
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

        assertThat(exchange.getIn().getBody(Actions.class).getActions()).contains(delayed0);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).contains(direct1);

        engine.setPointState(43, 1f);

        exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(0);
    }

    @Test
    public void should_handle_retrigger() throws Exception {
        // rule
        EventRule rule = new EventRule();
        rule.setName("Retrigger 43 on push on 42");
        rule.getConditions().add(new Condition(42, 1, ConditionType.event));
        rule.getConditions().add(new Condition(43, 0, ConditionType.state));
        Consequence direct1 = new Consequence(43, 1);
        Consequence delayed0 = new Consequence(43, 0, 1000, TriggerType.RETRIGGER);
        rule.getConsequences().add(direct1);
        rule.getConsequences().add(delayed0);
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        engine.registerPackages(build);

        Exchange exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(2);

        assertThat(exchange.getIn().getBody(Actions.class).getActions()).contains(delayed0);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).contains(direct1);

        engine.setPointState(43, 1f);

        exchange = buildExchange(1f);
        engine.process(exchange);
        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(1);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getOutPointId()).isEqualTo(43);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(0f);
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
