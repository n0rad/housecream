package net.awired.housecream.server.application.config;

import net.awired.client.bean.validation.js.service.ValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration()
public class ValidationConfig {

    @Bean(name = "validator")
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidatorFactory(localValidatorFactoryBean());
        return methodValidationPostProcessor;
    }

    @Bean
    public ValidationService validationService() {
        return new ValidationService();
    }

}
