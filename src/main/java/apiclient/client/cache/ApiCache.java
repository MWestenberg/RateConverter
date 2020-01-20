package apiclient.client.cache;

import apiclient.model.ApiModel;

public interface ApiCache {

    ApiModel readCacheFile();
    boolean hasCacheFile();
    boolean deleteCacheFile();
    boolean writeCacheFile(ApiModel model);
    ApiModel getModel();
}
