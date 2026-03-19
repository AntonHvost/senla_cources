package config;

import io.csv.GenericCSVService;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import util.DatabaseMigrator;

import javax.sql.DataSource;
import java.util.Properties;

@ComponentScan(basePackages = {"repository.impl","service", "controller", "facade", "config"})
@PropertySource("classpath:app.properties")
@EnableTransactionManagement
public class SpringConfig {
    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show-sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2-ddl-auto}")
    private String hibernateHbm2DdlAuto;

    @Value("${liquibase.change-log-file}")
    private String changeLogFile;

    @Value("${liquibase.default-schema}")
    private String defaultSchema;

    @Value("${liquibase.database-change-log-table-name}")
    private String databaseChangeLogTableName;

    @Value("${liquibase.database-change-log-lock-table-name}")
    private String databaseChangeLogLockTableName;


    @Bean
    DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLogFile);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setDatabaseChangeLogTable(databaseChangeLogTableName);
        liquibase.setDatabaseChangeLogLockTable(databaseChangeLogLockTableName);
        return liquibase;
    }

    @Bean
    @DependsOn("liquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("domain.model");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setPersistenceUnitName("repository");
        entityManagerFactory.setJpaProperties(getHibernateProperties());
        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.show-sql", hibernateShowSql);
        properties.setProperty("hibernate.hbm2ddl.auto", hibernateHbm2DdlAuto);
        return properties;
    }

    @Bean
    public GenericCSVService genericCSVService() {
        return new GenericCSVService();
    }
}
