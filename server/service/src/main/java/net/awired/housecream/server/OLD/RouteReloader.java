package net.awired.housecream.server.OLD;

import java.util.ArrayList;
import javax.inject.Inject;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.CamelContextFactoryBean;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class RouteReloader implements ApplicationContextAware {

    @Inject
    private CamelContext camelContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    ApplicationContext applicationContext;

    public String reload(String camelContextName) throws Exception {
        CamelContextFactoryBean camelContextFactory = applicationContext.getBean("&" + camelContextName,
                CamelContextFactoryBean.class);
        SpringCamelContext camelContext = camelContextFactory.getContext();
        camelContext.stop();
        camelContext.destroy();
        camelContextFactory.setContext(null);
        for (RouteBuilder rb : applicationContext.getBeansOfType(RouteBuilder.class).values()) {
            rb.getRouteCollection().setRoutes(new ArrayList<RouteDefinition>()); // must clear the routes! 
            rb.configure();
        }
        camelContextFactory.afterPropertiesSet();
        camelContextFactory.getContext(true).start();
        return "ok";
    }

}
