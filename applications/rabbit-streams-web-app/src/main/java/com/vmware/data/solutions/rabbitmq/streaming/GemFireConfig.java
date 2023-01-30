package com.vmware.data.solutions.rabbitmq.streaming;

import com.vmware.data.solutions.rabbitmq.streaming.domain.MessageBag;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.JSONFormatter;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Configuration
@ClientCacheApplication
@EnablePdx
public class GemFireConfig {

    @Value("${spring.data.gemfire.pool.default.locators}")
    private String locators;

    @Bean
    Function<byte[], PdxInstance> toPdx()
    {
        return bytes -> JSONFormatter.fromJSON(new String(bytes, StandardCharsets.UTF_8));
    }

    @Bean
    ClientRegionFactoryBean<String, MessageBag> region(GemFireCache cache)
    {
        var region = new ClientRegionFactoryBean<String, MessageBag> ();
        region.setCache(cache);
        region.setName("test");
        region.setDataPolicy(DataPolicy.EMPTY);
        return region;
    }

    @Bean
    Function<String, Region<String, MessageBag>> regionFactory()
    {
        Function<String, Region<String, MessageBag>> factory = queue -> {
            var cacheFactory = ClientCacheFactory.getAnyInstance();

            var region = cacheFactory.getRegion(queue);
            if(region != null)
                return (Region)region;

            var factoryRegion = cacheFactory.createClientRegionFactory(ClientRegionShortcut.PROXY);

            return (Region)factoryRegion.create(queue);
        };

        return factory;
    }
}
