package org.haowei.clusteredcache.util;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.logging.Log;
import org.infinispan.util.logging.LogFactory;

/**
 * Created by haowei on 3/7/14.
 */
public class ClusterValidation {
    private static int REPLICATION_TRY_COUNT = 60;
    private static int REPLICATION_TIME_SLEEP = 2000;

    public static int TIMEOUT = REPLICATION_TIME_SLEEP * REPLICATION_TIME_SLEEP;

    private static final String KEY = ClusterValidation.class.getName();

    public static boolean waitForClusterToForm(EmbeddedCacheManager cacheManager, int nodeId, int clusterSize){
        return new ClusterValidation(cacheManager.getCache("repCache"), nodeId, clusterSize).chechReplicationServeralTimes()>0;
    }

    private Log log = LogFactory.getLog(ClusterValidation.class);

    private final Cache<Object, Object> cache;
    private final int clusterSize;
    private final int nodeId;

    private ClusterValidation(Cache<Object, Object> cache, int nodeId, int clusterSize){
        this.cache = cache;
        this.clusterSize = clusterSize;
        this.nodeId = nodeId;
    }

    private int chechReplicationServeralTimes(){
        for (int i = 0; i<REPLICATION_TRY_COUNT; i++){
            tryToPut();
            try{
                Thread.sleep(REPLICATION_TIME_SLEEP);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            int replCount = replicationCount(clusterSize);
            if (replCount == clusterSize - 1){
                log.info("Cluster formed successfully!");
                tryToPut();
                return replCount;
            }
        }
        log.warn("Cluster failed to form!");
        return -1;
    }

    private void tryToPut(){
        int tryCount = 0;
        while (tryCount < 5){
            try {
                cache.put(key(nodeId), "true");
                return;
            }catch (Throwable e){
                tryCount++;
            }
        }
        throw new IllegalStateException("Couldn't accomplish addition before replication!");
    }

    private int replicationCount(int clusterSize){
        int replicaCount = 0;
        for (int i = 0; i < clusterSize; i++){
            if (i == nodeId){
                continue;
            }
            Object data = tryGet(i);
            if (data == null || !"true".equals(data)){

            }else{
                replicaCount++;
            }
        }
        return replicaCount;
    }

    private Object tryGet(int i){
        int tryCount = 0;
        while (tryCount < 5){
            try {
                return cache.get(key(i));
            }catch (Throwable e ){
                tryCount++;
            }
        }
        return null;
    }

    private String key(int slaveIndex){
        return KEY + slaveIndex;
    }

}
