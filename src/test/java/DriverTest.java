import config.AppConfig;
import apiclient.client.RestClient;
import apiclient.client.cache.RestCache;
import apiclient.drivers.ApiDriver;
import apiclient.drivers.FixerIODriver;
import apiclient.model.FixerIOModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class DriverTest {

    AppConfig appConfig;

    @Before
    public void setup() {
        appConfig = AppConfig.init();
    }

    @Test
    public void driver_can_force_update() throws Exception
    {

        ApiDriver apiDriver = new FixerIODriver();
        assertTrue(apiDriver.updateCache());

        FixerIOModel model = (FixerIOModel)apiDriver.getApiModel();
        assertTrue(model.isSuccess());

    }

    @Test
    public void driver_can_retrieve_data_from_cache() throws Exception
    {
        //Run the client first
        RestClient client = new RestClient(FixerIOModel.class);
        client.connect(appConfig.getDefaultRestUrl());

        //Write the cache file
        RestCache cache = new RestCache(appConfig.getCacheFileName(),FixerIOModel.class);
        cache.deleteCacheFile(); // delete it first just in case
        assertTrue(cache.writeCacheFile(client.getModel())); //write it.

        //Instantiate driver
        ApiDriver apiDriver = new FixerIODriver();

        FixerIOModel model = (FixerIOModel)apiDriver.getApiModel();
        assertTrue(model.isSuccess());

    }


}
