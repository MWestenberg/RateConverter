package apiclient.client;

import apiclient.model.ApiModel;

public interface ApiClient {
    boolean connect(String urlStr);
    ApiModel getModel();
}
