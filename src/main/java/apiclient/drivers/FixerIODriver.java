package apiclient.drivers;

import apiclient.ApiDriverException;
import apiclient.client.RestClient;
import apiclient.client.cache.RestCache;
import apiclient.model.ApiModel;
import apiclient.model.FixerIOModel;
import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixerIODriver implements ApiDriver {

    private static final Logger LOG = LoggerFactory.getLogger(FixerIODriver.class);

    private AppConfig appConfig = AppConfig.init();
    private static final String URL_PROPERTY = "app.fixerIO.rest.url";
    private static final String cacheFileName = "fixerIO.json";

    private RestClient apiClient;
    RestCache apiCache;
    private FixerIOModel apiModel;

    public FixerIODriver() throws ApiDriverException
    {
        if (appConfig.getProperty(URL_PROPERTY).isEmpty())
        {
            String message = "Url for driver "+getClass().getName()+ " is unknown. Please set in property "+URL_PROPERTY+ " in config file";
            LOG.warn(message);
            throw new ApiDriverException(message);
        }

        apiCache = new RestCache(cacheFileName, FixerIOModel.class);

    }

    @Override
    public boolean hasCacheFile()
    {
        return apiCache.hasCacheFile();
    }


    @Override
    public boolean deleteCache()
    {
        return  apiCache.deleteCacheFile();
    }

    @Override
    public boolean forceCacheUpdate()
    {
        apiClient = new RestClient(FixerIOModel.class);
        boolean response = apiClient.connect(appConfig.getProperty(URL_PROPERTY));
        if (response && ((FixerIOModel)apiClient.getModel()).isSuccess())
        {
            apiModel = (FixerIOModel)apiClient.getModel();
            apiCache.writeCacheFile(apiModel);
            return true;
        }

        LOG.error("Unable to force an update. Check your API properties.");
        return false;

    }

    @Override
    public boolean updateCache() {
        return updateCache(24);
    }

    @Override
    public boolean updateCache(int hours) {
        if (apiCache.hasCacheFile()
                && ((FixerIOModel)apiCache.readCacheFile()).isSuccess()
                && !apiCache.readCacheFile().isExpired(hours))
        {
            apiModel = (FixerIOModel)apiCache.getModel();
            return true;
        }
        LOG.info("Forcing an a new API call to update cache file");
        return forceCacheUpdate();
    }

    @Override
    public ApiModel getApiModel() {
        updateCache();
        return apiModel;
    }


}
