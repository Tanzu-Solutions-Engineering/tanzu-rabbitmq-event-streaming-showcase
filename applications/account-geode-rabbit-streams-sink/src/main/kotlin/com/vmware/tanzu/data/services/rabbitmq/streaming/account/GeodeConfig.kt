package com.vmware.tanzu.data.services.rabbitmq.streaming.account

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.client.ClientCacheFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.GemfireTemplate
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries

/**
 * @author Gregory Green
 */
@Configuration
@ClientCacheApplication(subscriptionEnabled = true)
@EnableContinuousQueries
class GeodeConfig {

    @Bean("Account")
    fun accountRegion(gemFireCache: GemFireCache) : ClientRegionFactoryBean<String,Account>
    {
        var region = ClientRegionFactoryBean<String,Account>()
        region.cache = gemFireCache
        region.setRegionName("Account")
        region.setDataPolicy(DataPolicy.EMPTY)
       return region

    }

    @Bean
    fun gemfireTemple(gemFireCache : GemFireCache,
     @Value("\${gemfire.region.name:Account}") regionName : String) : GemfireTemplate
    {
        return GemfireTemplate<String, Account>(
            ClientCacheFactory
                .getAnyInstance()
                .getRegion(regionName))
    }
}

