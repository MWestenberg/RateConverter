import apiclient.client.RestClient;
import apiclient.client.cache.RestCache;
import apiclient.model.FixerIOModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;


public class RestClientAndCacheTest {

    AppConfig appConfig;
    ObjectMapper objectMapper = new ObjectMapper();
    private String restUrl;
    private String cacheFileName;
    private RestClient rateRestClient;
    private Properties prop = new Properties();
    private final static String fileName = "app.config";

    @Before
    public void setup()
    {
        appConfig = AppConfig.init();
        can_read_config();
        restUrl = appConfig.getDefaultRestUrl();
        cacheFileName = appConfig.getCacheFileName();
    }

    @Test
    public void can_read_config() {

        assertEquals(appConfig.getAppName(), "Currency Converter");
        assertNotNull(appConfig.getAppVersion());
        assertNotNull(appConfig.getDefaultRestUrl());
        assertNotNull(appConfig.getCacheFileName());
    }

    @Test
    public void can_connect_to_typed_api_client()
    {
        rateRestClient = new RestClient(FixerIOModel.class);
        assertTrue(rateRestClient.connect(restUrl));
        assertTrue(((FixerIOModel) rateRestClient.getModel()).isSuccess());
    }

    @Test
    public void can_store_result_from_RestClient()
    {
        can_connect_to_typed_api_client();
        RestCache restCache = new RestCache(cacheFileName, FixerIOModel.class);
        restCache.deleteCacheFile(); //delete the file first
        assertFalse(restCache.hasCacheFile());
        assertTrue(restCache.writeCacheFile(rateRestClient.getModel()));
        assertTrue(restCache.hasCacheFile());

    }


    @Test
    public void can_cache_rest_call_and_retrieve_rate()
    {
        // first make a rest call and store the result in a file
        can_connect_to_typed_api_client();
        RestCache restCache = new RestCache(cacheFileName, FixerIOModel.class);
        restCache.deleteCacheFile(); //delete the file first
        assertTrue(restCache.writeCacheFile(rateRestClient.getModel()));
        assertTrue(restCache.hasCacheFile());
        assertTrue(((FixerIOModel)restCache.readCacheFile()).isSuccess());
        assertTrue(restCache.readCacheFile().hasRate("EUR"));
        assertEquals(restCache.readCacheFile().getRate("EUR").getRate(), 1.0, 0);

    }
}
