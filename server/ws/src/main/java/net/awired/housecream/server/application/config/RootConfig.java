package net.awired.housecream.server.application.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableAsync
@EnableScheduling
@EnableMBeanExport
@ComponentScan(basePackages = { "net.awired.housecream", "net.awired.ajsl" })
public class RootConfig implements AsyncConfigurer, SchedulingConfigurer {

    @Bean
    public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer placeholder = new PropertySourcesPlaceholderConfigurer();
        //        placeholder.setLocation(new ClassPathResource("/META-INF/spring/database.properties"));
        return placeholder;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(7);
        //        executor.setMaxPoolSize(42);
        //        executor.setQueueCapacity(11);
        executor.setThreadNamePrefix("Executor-");
        executor.initialize();
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
        //        taskRegistrar.addTriggerTask(new Runnable() {
        //            @Override
        //            public void run() {
        //                myTask().work();
        //            }
        //        }, new CustomTrigger());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(10);
    }
}
