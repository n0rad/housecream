package net.awired.housecream.server.engine.builder;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collection;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.PointState;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.engine.Actions;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.service.event.EventService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.drools.definition.KnowledgePackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RuleBuilderTest {

    @Mock
    private StateHolder stateHolder;

    @Mock
    private EventService eventService;

    @InjectMocks
    private RuleBuilder ruleBuilder = new RuleBuilder();

    @InjectMocks
    private EngineProcessor engineProcessor = new EngineProcessor();

    @Test
    public void should_build_rule() throws Exception {

        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(44, 1, ConditionType.event));
        rule.getConsequences().add(new Consequence(45, 1));
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);

        engineProcessor.registerPackages(build);

        Exchange exchange = new DefaultExchange((CamelContext) null);
        Message in = new DefaultMessage();
        Event e = new Event();
        e.setPointId(44);
        e.setValue(1f);
        in.setBody(e);
        exchange.setIn(in);

        engineProcessor.process(exchange);

        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getPointId()).isEqualTo(45);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(1f);
    }

    @Test
    public void should_build_rule2() throws Exception {
        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(42, 1, ConditionType.event));
        rule.getConditions().add(new Condition(43, 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(43, 1));
        Collection<KnowledgePackage> build = ruleBuilder.build(rule);
        // rule 2
        EventRule rule2 = new EventRule();
        rule2.setName("my first rule2");
        rule2.getConditions().add(new Condition(42, 1, ConditionType.event));
        rule2.getConditions().add(new Condition(43, 1, ConditionType.state));
        rule2.getConsequences().add(new Consequence(43, 0));
        Collection<KnowledgePackage> build2 = ruleBuilder.build(rule2);
        engineProcessor.registerPackages(build);
        engineProcessor.registerPackages(build2);

        Exchange exchange = buildExchange(1f);

        engineProcessor.process(exchange);

        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(1);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getPointId()).isEqualTo(43);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(0f);

        exchange = buildExchange(1f);

        when(stateHolder.getFacts()).thenReturn(Arrays.asList((Object) new PointState(43, 0f)));
        engineProcessor.process(exchange);

        assertThat(exchange.getIn().getBody(Actions.class).getActions()).hasSize(1);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getPointId()).isEqualTo(43);
        assertThat(exchange.getIn().getBody(Actions.class).getActions().get(0).getValue()).isEqualTo(1f);

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
