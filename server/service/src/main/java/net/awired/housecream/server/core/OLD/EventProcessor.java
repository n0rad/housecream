package net.awired.housecream.server.core.OLD;

import net.awired.housecream.server.core.domain.HcEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EventProcessor implements Processor {

    //    from(x).process(mySpecialProcessor).to(direct:a).to(zzz)
    //
    //    And MySpecialProcessor implements Processor {
    //
    //    public void process(Exchange exchange) {
    //      // if special condition
    //     ProducerTemplate template =
    //    exchange.getCamelContext().createProducerTemplate();
    //      template.start();
    //      template.send("direct:a", myNewExchange)
    //      template.stop();
    //    }
    //
    //    Ahhh there is this FAQ
    //    http://camel.apache.org/how-do-i-write-a-custom-processor-which-sends-multiple-messages.html
    //
    //    And this FAQ about you should invoke stop() on producer template when finished:
    //    http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn(HcEvent.class);

        exchange.getOut().setBody("genre");
    }
}
