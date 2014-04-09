package org.haowei.clusteredcache.distribution;

import org.haowei.clusteredcache.util.LoggingListener;
import org.infinispan.Cache;

/**
 * Created by hao on 3/12/14.
 */
public class Node2 extends AbstractNode {

    public static void main(String[] args) throws Exception{
        new Node2().run();
    }

    public void run(){
        Cache<String, String> cache = getCacheManager().getCache("distCache");

        cache.addListener(new LoggingListener());

        waitForClusterToForm();

        putData(cache);

    }
    @Override
    protected int getNodeId(){
        return 2;
    }

    protected void putData(Cache<String,String> cache){
        for (int i =0; i<10000; i++){
            cache.put(""+(i+100), "true"+i);
        }
    }

}
