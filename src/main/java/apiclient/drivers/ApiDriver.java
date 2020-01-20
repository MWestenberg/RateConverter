package apiclient.drivers;

import apiclient.model.ApiModel;

public interface ApiDriver
{
    boolean hasCacheFile();
    boolean forceCacheUpdate();
    boolean updateCache();
    boolean updateCache(int hours);
    ApiModel getApiModel();
    boolean deleteCache();
}