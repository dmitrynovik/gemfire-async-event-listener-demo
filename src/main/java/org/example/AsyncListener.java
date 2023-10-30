package org.example;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;

import java.util.List;
import java.util.Properties;

public class AsyncListener implements AsyncEventListener {

  private LogWriter logger;

  @Override
  public void initialize(Cache cache, Properties properties) {
    AsyncEventListener.super.initialize(cache, properties);
    logger = cache.getLogger();
  }

  @Override
  public boolean processEvents(List<AsyncEvent> list) {

    logger = CacheFactory.getAnyInstance().getLogger();
    for (AsyncEvent asyncEvent : list) {
      if (asyncEvent.getOperation().isCreate()) {
        logger.info("Received Create event: Key: " + asyncEvent.getKey() + "  Value: " + asyncEvent.getDeserializedValue());
      }
      if (asyncEvent.getOperation().isUpdate()) {
        logger.info("Received Update event: Key: " + asyncEvent.getKey() + "  Value: " + asyncEvent.getDeserializedValue());
      }
      if (asyncEvent.getOperation().isDestroy()) {
        logger.info("Received Destroy update: Key: " + asyncEvent.getKey() + "  Value: " + asyncEvent.getDeserializedValue());
      }
    }
    return true;
  }
}
