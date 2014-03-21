package org.haowei.clusteredcache.util;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.util.logging.LogFactory;
import org.infinispan.util.logging.Log;


/**
 * Created by haowei on 3/7/14.
 */
@Listener
public class LoggingListener {
    private Log log = LogFactory.getLog(LoggingListener.class);

    @CacheEntryCreated
    public void observeAdd(CacheEntryCreatedEvent<?,?> event){
        if (!event.isPre())
            log.infov("Cache entry with key "+event.getKey()+" added in cache "+event.getCache());

    }

    @CacheEntryRemoved
    public void observeRemove(CacheEntryRemovedEvent<?,?> event){
        log.infof("Cache entry with key %s removed in cache %s", event.getKey(), event.getCache());
    }


}
