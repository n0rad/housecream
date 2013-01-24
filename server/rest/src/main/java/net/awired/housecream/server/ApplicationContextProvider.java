package net.awired.housecream.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;
    private static ApplicationContextProvider provider = new ApplicationContextProvider();

    private ApplicationContextProvider() {
    }

    public static ApplicationContextProvider getInstance() {
        return provider;
    }

    public <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.context = applicationContext;
    }

}
