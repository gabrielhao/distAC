package org.haowei.clusteredcache.replication;

import org.haowei.clusteredcache.util.LoggingListener;
import org.infinispan.Cache;

/**
 * Created by hao on 3/27/14.
 */
public class repNode1 extends AbstractNode {
    @Override
    protected int getNodeId() {
        return 1;
    }

    public static void main(String[] args) throws Exception{
        new repNode1().run();
    }
    public void run(){
        Cache<String, String> cache = getCacheManager().getCache("repCache");
        cache.addListener(new LoggingListener());
        putData(cache);
        waitForClusterToForm();


    }

    void putData(Cache<String, String> cache){
        for (int i =0; i<100; i++){
            cache.put(""+(i+100), "true"+i);
        }
    }
}
