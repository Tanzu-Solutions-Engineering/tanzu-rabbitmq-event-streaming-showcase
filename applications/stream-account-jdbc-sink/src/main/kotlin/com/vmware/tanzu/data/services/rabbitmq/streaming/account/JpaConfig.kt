package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.*
import javax.sql.DataSource


/**
 * @author Gregory Green
 */
@Configuration
@EnableJpaRepositories
class JpaConfig {
    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean? {

        //See https://docs.jboss.org/hibernate/orm/5.0/manual/en-US/html/ch03.html
        val properties : Properties = Properties()
        properties.setProperty("hibernate.hbm2ddl.auto","create")
        properties.setProperty("hibernate.show_sql","true")

        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan(*arrayOf("com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain"))
        val vendorAdapter: JpaVendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        em.setJpaProperties(properties)
//        em.setJpaProperties(additionalProperties())
        return em
    }
}