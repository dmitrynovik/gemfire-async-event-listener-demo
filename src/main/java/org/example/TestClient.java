package org.example;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

public class TestClient {
  public static void main(String[] args) {
    ClientCache clientCache = new ClientCacheFactory().addPoolLocator("localhost", 10334).create();
    Region<Object, Object> testRegion = clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY).create("TestRegion");

    for (int i = 0; i < 10; i++) {
      testRegion.put(i,i+"-Value");
    }
  }
}
