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
    }
    @Override
    protected int getNodeId(){
        return 2;
    }

}
