package com.vmare.account.gemfire.rabbit.stream.sink;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@ClientCacheApplication(subscriptionEnabled = true)
@EnableContinuousQueries
@EnableGemfireRepositories
@EnablePdx
public class GemFireConfig {
    @Bean("Account")
    ClientRegionFactoryBean<String, Account> accountRegion(GemFireCache gemFireCache)
    {
        var region = new ClientRegionFactoryBean<String, Account>();
        region.setCache(gemFireCache);
        region.setRegionName("Account");
        region.setDataPolicy(DataPolicy.EMPTY);
        return region;
    }

    @Bean
    GemfireTemplate gemfireTemple(GemFireCache gemFireCache ,
                                  @Value("${gemfire.region.name:Account}")
                                  String regionName)
    {
        return new GemfireTemplate(
            ClientCacheFactory
                    .getAnyInstance()
                    .getRegion(regionName));
    }

}
