@Configuration  // <-- Added the missing @Configuration annotation
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager",
    basePackages = {
        "com.sc.rdc.cops.ivrapi.repository.sg",
        "com.sc.rdc.cops.ivrapi.repository",
        "com.sc.rdc.cops.ivrapi.model.sg",  // <-- Fixed "nodel" typo to "model.sg"
        "com.sc.rdc.cops.ivrapi.model"
    }
)
public class SGDataSourceConfig {

    @Primary
    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource(@Qualifier("dataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {  // <-- Corrected the method signature to accept dataSource

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();  // <-- Moved vendorAdapter inside the method

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);

        factory.setPackagesToScan(
            "com.sc.rdc.cops.ivrapi.model.sg",
            "com.sc.rdc.cops.ivrapi.model",
            "com.sc.rdc.cops.ivrapi.repository.sg",
            "com.sc.rdc.cops.ivrapi.repository"
        );  // <-- Fixed "ivros" typo to "repository.sg" and fixed package paths

        factory.setDataSource(dataSource);  // <-- Pass the dataSource here

        return factory;  // <-- Return the factory
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}




@Configuration   // <-- Added the missing @Configuration annotation
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "myEntityManagerFactory",
    transactionManagerRef = "transactionManagerMY",
    basePackages = {
        "com.sc.rdc.cops.ivrapi.repository.my",
        "com.sc.rdc.cops.ivrapi.repository",
        "com.sc.rdc.cops.ivrapi.model.my",
        "com.sc.rdc.cops.ivrapi.model"
    }
)
public class MYDataSourceConfig {

    @Bean(name = "myDataSourceProperties")
    @ConfigurationProperties("spring.datasource.my")
    public DataSourceProperties myDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "myDataSource")
    @ConfigurationProperties("spring.datasource.my")
    public DataSource dataSource(@Qualifier("myDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "myEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("myDataSource") DataSource dataSource) {  // <-- Corrected the method signature to accept the datasource

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();  // <-- Moved vendorAdapter inside the method

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);

        factory.setPackagesToScan(
            "com.sc.rdc.cops.ivrapi.model.my",
            "com.sc.rdc.cops.ivrapi.model",
            "com.sc.rdc.cops.ivrapi.repository.my",
            "com.sc.rdc.cops.ivrapi.repository"
        );
        factory.setDataSource(dataSource);  // <-- Pass the datasource here

        return factory;  // <-- Return the factory
    }

    @Bean(name = "transactionManagerMY")
    public PlatformTransactionManager transactionManager(
            @Qualifier("myEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
