package config;

import apiclient.model.ApiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    public static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);
    private final static String APP_NAME = "app.name";
    private final static String APP_VERSION = "app.version";
    private final static String APP_DEFAULT_REST_URL = "app.default.rest.url";
    private final static String APP_DEFAULT_REST_CACHE = "app.default.rest.cache";
    private final static String APP_DEFAULT_REST_MODEL = "app.default.rest.model";

    public static AppConfig appConfig;
    private final static String fileName = "app.config";
    private Properties properties;

    public static AppConfig init()
    {
        if (null == appConfig)
            appConfig = new AppConfig();

        return appConfig;
    }

    public String getAppName()
    {
        return getProperty(APP_NAME);
    }

    public String getCacheFileName()
    {
        return getProperty(APP_DEFAULT_REST_CACHE);
    }

    public String getDefaultRestUrl()
    {
        return getProperty(APP_DEFAULT_REST_URL);
    }

    public ApiModel getDefaultRestModel()
    {
        String defaultClass = getProperty(APP_DEFAULT_REST_MODEL);
        ApiModel defaultModel = null;
        try {
            defaultModel = (ApiModel) Class.forName(defaultClass).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOG.debug(e.getMessage());
            LOG.warn("Default model not configured in "+fileName);

        }
        return defaultModel;

    }

    public String getAppVersion()
    {
        return getProperty(APP_VERSION);
    }


    public String getProperty(String name)
    {
        if (properties.containsKey(name))
            return properties.getProperty(name);
        return "";
    }

    public boolean setProperty(String key, String value)
    {
        if (!properties.containsKey(key))
        {
            properties.setProperty(key,value);
            return true;
        }
        return false;
    }

    private AppConfig() {
        properties = new Properties();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream;

        try {
            inputStream = classloader.getResourceAsStream(fileName);
        } catch (NullPointerException ex) {
            System.out.println("Nullpointer Exception: "+fileName +" not found");
            return;
        }
        try {
            if (inputStream == null)
                throw new NullPointerException("Input stream is null");
            properties.load(inputStream);
        } catch (IOException ex) {
            System.out.println("Unable to load properties file");
        }

    }
}
