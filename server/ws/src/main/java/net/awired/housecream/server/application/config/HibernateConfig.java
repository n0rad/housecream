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
