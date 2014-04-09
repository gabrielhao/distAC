package org.haowei.clusteredcache.replication;

import org.haowei.clusteredcache.util.ClusterValidation;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.loaders.mongodb.configuration.MongoDBCacheStoreConfiguration;
import org.infinispan.loaders.mongodb.configuration.MongoDBCacheStoreConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.util.IllegalFormatException;

/**
 * Created by hao on 3/7/14.
 */
public abstract class AbstractNode {

    private static EmbeddedCacheManager createCacheManagerProgramatically(){
        ConfigurationBuilder b = new ConfigurationBuilder();
        b.eviction().maxEntries(100).expiration().wakeUpInterval(100).lifespan(1000).maxIdle(500).loaders().addStore(MongoDBCacheStoreConfigurationBuilder.class)
                .host("localhost")
                .port(27017)
                .timeout(1500)
                .acknowledgment(0)
                .username("mongo")
                .password("mongo")
                .database("infinispan_cachestore")
                .collection("entries")
                .clustering().cacheMode(CacheMode.REPL_SYNC);

        Configuration config = b.build();

        MongoDBCacheStoreConfiguration store = (MongoDBCacheStoreConfiguration) config.loaders().cacheLoaders().get(0);

        GlobalConfiguration globalConf = GlobalConfigurationBuilder.defaultClusteredBuilder().transport()
                .addProperty("configurationFile","jgroups.xml").build();
        EmbeddedCacheManager cacheManager = new DefaultCacheManager(globalConf);
        cacheManager.defineConfiguration("repCache", config);
        return cacheManager;
    }

    public static final int CLUSTER_SIZE = 2;

    private final EmbeddedCacheManager cacheManager;

    public AbstractNode(){
        this.cacheManager = createCacheManagerProgramatically();
    }

    protected EmbeddedCacheManager getCacheManager(){ return cacheManager;}

    protected void waitForClusterToForm(){
        if (!ClusterValidation.waitForClusterToForm(getCacheManager(), getNodeId(), CLUSTER_SIZE)){
            throw new IllegalStateException("Error forming cluster");
        }
    }

    protected abstract int getNodeId();
}
