package net.awired.housecream.server.application.config;

import javax.inject.Inject;
import net.awired.housecream.server.router.StaticRouteManager;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CamelRouteConfig extends SingleRouteCamelConfiguration {

    @Inject
    private StaticRouteManager manager;

    //    @Override
    //    protected CamelContext createCamelContext() throws Exception {
    //        SpringCamelContextFactory factory = new SpringCamelContextFactory();
    //        factory.setApplicationContext(getApplicationContext());
    //        return factory.createContext();
    //    }

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        //        // setup the ActiveMQ component
        //        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        //        connectionFactory
        //                .setBrokerURL("vm://localhost.spring.javaconfig?marshal=false&broker.persistent=false&broker.useJmx=false");
        //
        //        // and register it into the CamelContext
        //        JmsComponent answer = new JmsComponent();
        //        answer.setConnectionFactory(connectionFactory);
        //        camelContext.addComponent("jms", answer);
    }

    @Bean
    @DependsOn("applicationContextProvider")
    @Override
    public RouteBuilder route() {
        return manager;
    }

}
