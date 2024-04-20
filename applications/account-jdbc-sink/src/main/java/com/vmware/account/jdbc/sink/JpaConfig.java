package com.vmware.account.jdbc.sink;

import com.vmware.account.jdbc.sink.domain.AccountEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories
public class JpaConfig {

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        var properties  = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto","create");
        properties.setProperty("hibernate.show_sql","true");

        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(AccountEntity.class.getPackageName());
        var vendorAdapter  = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(properties);
        return em;
    }
}
