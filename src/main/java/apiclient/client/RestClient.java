package apiclient.client;

import apiclient.model.ApiModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.URL;

/**
 * Class connects to RestFul service
 * It visits the API only once a day to limit it's use
 *
 */
public class RestClient implements ApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    private static final AppConfig appConfig = AppConfig.init();
    private ObjectMapper objectMapper;
    private Class<?> typeParameterClass;
    private ApiModel model;


    /**
     * Requires an API Model als class type
     * @param typeParameterClass the type of class for the API Model i.e. FixerIOModel.class
     */
    public RestClient(Class<?> typeParameterClass)
    {
        this.typeParameterClass = typeParameterClass;
        objectMapper = new ObjectMapper();
    }

    /**
     * Will try to connect to the API and map against the object type provided in the
     * Construtor
     * @param urlStr the Rest url you would like to connect to
     * @return boolean, tru of connection was succesfull
     */
    @Override
    public boolean connect(String urlStr)
    {
        try
        {
            URL url = new URL(urlStr);
            Type modelType = getClass().getGenericSuperclass();
            model = (ApiModel)objectMapper.readValue(url, typeParameterClass);
        }catch (Exception ex)
        {
            LOG.error("Unable to connect to RESTful API: "+urlStr);
            LOG.warn(ex.getMessage());
            return false;
        }
        return true;
    }


    /**
     *
     * @return the API model
     */
    @Override
    public ApiModel getModel() {
        if (model ==null)
            LOG.warn("The instance of model is not of type ApiModel");

        return model;
    }



}
