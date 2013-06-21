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
package net.awired.housecream.server.application.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.h2.tools.Server;
import org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class DatabaseConfig {

    @Value("${database.class:org.h2.Driver}")
    private String databaseClass;

    @Value("${hibernate.dialect:org.hibernate.dialect.H2Dialect}")
    private String hibernateDialect;

    @Value("${hibernate.hbm2ddl.auto:update}")
    private String ddl;

    @Value("${hibernate.show_sql:false}")
    private String showSql;

    @Value("${database.url:jdbc:h2:file:${HOUSECREAM_HOME}/db/housecream-database;TRACE_LEVEL_FILE=2;TRACE_LEVEL_SYSTEM_OUT=0}")
    private String databaseUrl;

    @Value("${database.username:sa}")
    private String databaseUsername;

    @Value("${database.password:}")
    private String databasePassword;

    @Lazy(false)
    @Bean(name = "org.h2.tools.Server", initMethod = "start", destroyMethod = "stop")
    @DependsOn("entityManagerFactory")
    public Server server() throws Exception {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8044");
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseClass);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.connection.driver_class", databaseClass);
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.hbm2ddl.auto", ddl);
        jpaProperties.put("hibernate.show_sql", showSql);
        jpaProperties.put("hibernate.format_sql", true);
        jpaProperties.put("hibernate.generate_statistics", true);
        jpaProperties.put("hibernate.jdbc.batch_size", 0);
        jpaProperties.put("hibernate.validator.apply_to_ddl", false);
        jpaProperties.put("hibernate.ejb.naming_strategy", org.hibernate.cfg.ImprovedNamingStrategy.class.getName());

        jpaProperties.put("hibernate.archive.autodetection", "class");

        jpaProperties.put("hibernate.connection.url", databaseUrl);
        jpaProperties.put("hibernate.connection.username", databaseUsername);
        jpaProperties.put("hibernate.connection.password", databasePassword);

        jpaProperties.put("hibernate.connection.provider_class", C3P0ConnectionProvider.class.getName());
        jpaProperties.put("hibernate.c3p0.min_size", 5);
        jpaProperties.put("hibernate.c3p0.max_size", 20);
        jpaProperties.put("hibernate.c3p0.max_statements", 50);

        //        jpaProperties.put("hibernate.c3p0.timeout", 10); // sec
        //        jpaProperties.put("c3p0.idle_test_period", 5); // sec

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaProperties(jpaProperties);
        factoryBean.setPackagesToScan(new String[] { "net.awired.housecream" });

        return factoryBean;
    }

    ////////////////////////////////////

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return transactionManager;
    }

}
