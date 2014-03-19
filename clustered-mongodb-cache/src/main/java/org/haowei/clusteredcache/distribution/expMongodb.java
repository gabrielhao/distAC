package org.haowei.clusteredcache.distribution;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.loaders.mongodb.configuration.MongoDBCacheStoreConfiguration;
import org.infinispan.loaders.mongodb.configuration.MongoDBCacheStoreConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Created by hao on 3/17/14.
 */
public class expMongodb {

    public static void main(String[] args){
        EmbeddedCacheManager cacheManager = createCacheManagerProgramatically();
        Cache<String, String> cache = cacheManager.getCache("distCache");
    }
    private static EmbeddedCacheManager createCacheManagerProgramatically(){
        ConfigurationBuilder b = new ConfigurationBuilder();
        b.loaders().addStore(MongoDBCacheStoreConfigurationBuilder.class)
                .host("localhost")
                .port(27017)
                .timeout(1500)
                .acknowledgment(0)
                .username("mongo")
                .password("mongo")
                .database("infinispan_cachestore")
                .collection("entries")
                .clustering().cacheMode(CacheMode.DIST_SYNC).hash().numOwners(2);

        Configuration config = b.build();

        MongoDBCacheStoreConfiguration store = (MongoDBCacheStoreConfiguration) config.loaders().cacheLoaders().get(0);

        GlobalConfiguration globalConf = GlobalConfigurationBuilder.defaultClusteredBuilder().transport()
                .addProperty("configurationFile","jgroups.xml").build();
        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConf);
        cacheManager.defineConfiguration("distCache", config);
        return cacheManager;
    }

}
