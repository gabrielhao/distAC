package org.haowei.clusteredcache.distribution;

import org.haowei.clusteredcache.util.LoggingListener;
import org.infinispan.Cache;

/**
 * Created by haowei on 3/12/14.
 */
public class Node1 extends AbstractNode {
    @Override
    protected int getNodeId(){
        return 1;
    }

    public static void main(String[] args) throws Exception{
        new Node1().run();
    }

    public void run(){
        Cache<String, String> cache = getCacheManager().getCache("distCache");

        cache.addListener(new LoggingListener());

        waitForClusterToForm();
    }

}

