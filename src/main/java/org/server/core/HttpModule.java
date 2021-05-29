package org.server.core;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class HttpModule {

    private HttpModule() {
        try {
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void build() throws Exception {
        httpClient  = new HttpClient();
        httpClient.start();
    }

    public void execute(String url) throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println(url);
        ContentResponse response = httpClient.GET(url);
        System.out.println(response);
    }

    public static final HttpModule module()
    {
        return module_;
    }
    private HttpClient httpClient;
    private static final HttpModule module_ = new HttpModule();
}
