/*
 *
 *  * Copyright 2023 VMware, Inc.
 *  * SPDX-License-Identifier: GPL-3.0
 *
 */
package showcase.event.stream.rabbitmq.accoun.sink;

import com.vmware.tanzu.data.services.rabbitmq.streaming.account.domain.Account;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@ClientCacheApplication
@EnableSecurity
@Configuration
@EnablePdx
@EnableGemfireRepositories
public class GemFireConf
{
    @Value("${gemfire.region.name:Account}")
    private String regionName;

    @Bean
    ClientRegionFactoryBean<String, Account> account(GemFireCache gemFireCache)
    {
        var bean = new ClientRegionFactoryBean<String,Account>();
        bean.setCache(gemFireCache);
        bean.setDataPolicy(DataPolicy.EMPTY);
        bean.setName(regionName);
        return bean;
    }
}
