package com.nic.billingstats.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.nic.billingstats.repository.p2.billing",
        entityManagerFactoryRef = "p2BillingEntityManager",
        transactionManagerRef = "p2BillingTransactionManager")
public class PersistenceP2BillingAutoConfiguration {

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Bean
    @ConfigurationProperties(prefix="spring.p2-billing-datasource")
    public DataSource p2BillingDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "p2BillingEntityManager")
    public LocalContainerEntityManagerFactoryBean p2BillingEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(p2BillingDataSource());
        em.setPackagesToScan(new String[] { "com.nic.billingstats.repository.entity.p2.billing" });
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", hibernateDialect);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "p2BillingTransactionManager")
    public PlatformTransactionManager p2BillingTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(p2BillingEntityManager().getObject());
        return transactionManager;
    }
}
