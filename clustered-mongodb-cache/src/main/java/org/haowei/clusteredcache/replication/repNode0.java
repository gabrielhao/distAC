package org.haowei.clusteredcache.replication;

import org.haowei.clusteredcache.util.LoggingListener;
import org.infinispan.Cache;

/**
 * Created by hao on 3/27/14.
 */
public class repNode0 extends AbstractNode {
    public static void main(String[] args) throws Exception{
        new repNode0().run();
    }
    public void run(){
        Cache<String, String> cache = getCacheManager().getCache("repCache");
        cache.addListener(new LoggingListener());

        waitForClusterToForm();

    }

    @Override
    protected int getNodeId() {
        return 0;
    }
}
