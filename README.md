To insert or update data into both databases in your Spring Boot application, you can configure multiple DataSource beans and use Spring's @Transactional to manage transactions across both databases. You will need to define the data sources separately and use JpaTransactionManager for each.

Here's an outline of what you need to do:

1. Define Multiple DataSources in Configuration

You need to define both data sources (for RDC_CVD_APP and CEMS_CC_PRD_PORTAL_APP) in your Spring Boot application.

@Configuration
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSourceRdcCvd() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.my")
    public DataSource dataSourceCemsCc() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryRdcCvd(
            EntityManagerFactoryBuilder builder, @Qualifier("dataSourceRdcCvd") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.package.rdc") // specify the package where RDC_CVD entities are
                .persistenceUnit("rdcCvd")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryCemsCc(
            EntityManagerFactoryBuilder builder, @Qualifier("dataSourceCemsCc") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.package.cems") // specify the package where CEMS_CC entities are
                .persistenceUnit("cemsCc")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManagerRdcCvd(
            @Qualifier("entityManagerFactoryRdcCvd") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public PlatformTransactionManager transactionManagerCemsCc(
            @Qualifier("entityManagerFactoryCemsCc") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

2. Specify Repositories for Each DataSource

Annotate each repository with @Transactional and specify which transaction manager to use by its transactionManager qualifier. Create separate repositories for each database, for example:

@Repository
public interface FeeWaiverRepositoryRdcCvd extends JpaRepository<FeeWaiver, String> {
    Optional<FeeWaiver> findById(String cardNum);
}

@Repository
public interface FeeWaiverRepositoryCemsCc extends JpaRepository<FeeWaiverMY, String> {
    Optional<FeeWaiverMY> findById(String cardNum);
}

3. Service Layer to Handle Transactions Across Both Databases

To update both databases within the same method, you can mark the method as @Transactional and ensure it operates on both repositories:

@Service
public class FeeWaiverService {

    @Autowired
    private FeeWaiverRepositoryRdcCvd feeWaiverRepositoryRdcCvd;

    @Autowired
    private FeeWaiverRepositoryCemsCc feeWaiverRepositoryCemsCc;

    @Transactional
    public Boolean updateFeeWaiver(FeeWaiverDto dto) {
        String cardNum = dto.getCardNum();

        // Update first database (RDC_CVD_APP)
        Optional<FeeWaiver> feeWaiverRdc = feeWaiverRepositoryRdcCvd.findById(cardNum);
        if (feeWaiverRdc.isPresent()) {
            FeeWaiver dbFeeWaiverRdc = feeWaiverRdc.get();
            dbFeeWaiverRdc.setAnnualFeeRequested(dto.getAnnualFeeRequested());
            dbFeeWaiverRdc.setAnnualFeeReqDate(dto.getAnnualFeeRequestedDate());
            dbFeeWaiverRdc.setLateFeeRequested(dto.getLateFeeRequested());
            dbFeeWaiverRdc.setLateFeeReqDate(dto.getLateFeeRequestedDate());
            feeWaiverRepositoryRdcCvd.save(dbFeeWaiverRdc);
        }

        // Update second database (CEMS_CC_PRD_PORTAL_APP)
        Optional<FeeWaiverMY> feeWaiverCems = feeWaiverRepositoryCemsCc.findById(cardNum);
        if (feeWaiverCems.isPresent()) {
            FeeWaiverMY dbFeeWaiverCems = feeWaiverCems.get();
            dbFeeWaiverCems.setAnnualFeeRequested(dto.getAnnualFeeRequested());
            dbFeeWaiverCems.setAnnualFeeReqDate(dto.getAnnualFeeRequestedDate());
            dbFeeWaiverCems.setLateFeeRequested(dto.getLateFeeRequested());
            dbFeeWaiverCems.setLateFeeReqDate(dto.getLateFeeRequestedDate());
            feeWaiverRepositoryCemsCc.save(dbFeeWaiverCems);
        }

        return true;
    }
}

4. Handle Transactions Across Multiple Databases

You can define global transaction behavior with @EnableTransactionManagement and ensure that transactions span both databases if needed.

@SpringBootApplication
@EnableTransactionManagement
public class IvrApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(IvrApiApplication.class, args);
    }
}

Summary:

Define multiple DataSource beans for both databases.

Create repositories for each database.

Use @Transactional in the service layer to manage updates in both databases.

Ensure correct package scanning for entities related to each database.


This will allow you to update both databases within the same method.

