package org.haowei.clusteredcache.distribution;

import org.haowei.clusteredcache.util.ClusterValidation;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.loaders.mongodb.configuration.MongoDBCacheStoreConfiguration;
import org.infinispan.loaders.mongodb.configuration.MongoDBCacheStoreConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import java.io.IOException;

/**
 * Created by hao on 3/7/14.
 */
public abstract class AbstractNode {

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
                .collection("entries");

        final Configuration config = b.build();
        MongoDBCacheStoreConfiguration store = (MongoDBCacheStoreConfiguration) config.loaders().cacheLoaders().get(0);

        return new DefaultCacheManager(GlobalConfigurationBuilder.defaultClusteredBuilder().transport()
        .addProperty("configurationFile","jgroups.xml").build(),
               b.clustering().cacheMode(CacheMode.DIST_SYNC).hash().numOwners(2).build());
    }

    private static EmbeddedCacheManager createCacheManagerFromXML() throws IOException{
        return new DefaultCacheManager("infinispan-distribution.xml");
    }

    public static final int CLUSTER_SIZE = 3;

    private final EmbeddedCacheManager cacheManager;

    public AbstractNode(){
        this.cacheManager = createCacheManagerProgramatically();
    }

    protected EmbeddedCacheManager getCacheManager(){
        return cacheManager;
    }

    protected  void waitForClusterToForm(){
        if (!ClusterValidation.waitForClusterToForm(getCacheManager(), getNodeId(), CLUSTER_SIZE)){
            throw new IllegalStateException("Error form cluster, check the log");
        }
    }

    protected abstract int getNodeId();





}
