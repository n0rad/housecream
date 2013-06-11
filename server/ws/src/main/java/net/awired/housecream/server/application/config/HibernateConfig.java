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


public class HibernateConfig {

    //    @Bean
    //    public DataSource getDataSource() {
    //        DriverManagerDataSource ds = new DriverManagerDataSource();
    //        ds.setDriverClassName(driverClassName);
    //        ds.setUrl(url);
    //        ds.setUsername(username);
    //        ds.setPassword(password);
    //        return ds;
    //    }
    //
    //    @Bean
    //    @Autowired
    //    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
    //        HibernateTransactionManager htm = new HibernateTransactionManager();
    //        htm.setSessionFactory(sessionFactory);
    //        return htm;
    //    }
    //
    //    @Bean
    //    @Autowired
    //    public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory) {
    //        HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
    //        return hibernateTemplate;
    //    }
    //
    //    @Bean
    //    public AnnotationSessionFactoryBean getSessionFactory() {
    //        AnnotationSessionFactoryBean asfb = new AnnotationSessionFactoryBean();
    //        asfb.setDataSource(getDataSource());
    //        asfb.setHibernateProperties(getHibernateProperties());
    //        asfb.setPackagesToScan(new String[] { "com.sivalabs" });
    //        return asfb;
    //    }
    //
    //    @Bean
    //    public Properties getHibernateProperties() {
    //        Properties properties = new Properties();
    //        properties.put("hibernate.dialect", hibernateDialect);
    //        properties.put("hibernate.show_sql", hibernateShowSql);
    //        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
    //
    //        return properties;
    //    }

}
