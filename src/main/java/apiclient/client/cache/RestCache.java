package apiclient.client.cache;

import apiclient.client.RestClient;
import apiclient.model.ApiModel;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RestCache implements ApiCache {

    private static final Logger LOG = LoggerFactory.getLogger(RestClient.class);

    private String fileName;
    private File file;
    private ObjectMapper mapper;
    private Class<?> typeParameterClass;
    private ApiModel model;


    public RestCache(String fileName, Class<?> typeParameterClass)
    {
        this.typeParameterClass = typeParameterClass;
        this.fileName = fileName;
        this.mapper = new ObjectMapper();

        Path source = Paths.get(this.getClass().getResource("/").getPath());
        this.file = new File(source.toString(), fileName);

    }

    public ApiModel readCacheFile() {
        if (hasCacheFile())
            return model;
        else
            return null;
    }

    public boolean hasCacheFile() {

        if (!file.isFile())
        {
            return false;
        }
        //so the file does exist return the file
        try {
            this.model = (ApiModel) mapper.readValue(file.toURI().toURL(), typeParameterClass);
        } catch (IOException e) {
            LOG.warn(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteCacheFile()
    {
        if (file.isFile())
        {
            try
            {
                return file.delete();
            }catch (Exception ex)
            {
                LOG.warn("File "+file.getName() + " cannot be deleted.");
                LOG.debug(ex.getMessage());
            }
            return  true;
        }
        else
        {
            LOG.info("File does not exist so nothing to delete");
        }
        return false;
    }


    public boolean writeCacheFile(ApiModel model)
    {
        try
        {
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(file, model);
            this.model = model;
        }catch (IOException ex)
        {
            LOG.warn("Unable to write to file");
            LOG.debug(ex.getMessage());
            return false;
        }
        return true;

    }

    public ApiModel getModel() {
        if (model == null)
            LOG.warn("Requested model is null");
        return model;
    }
}
