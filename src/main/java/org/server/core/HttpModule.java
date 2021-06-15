package org.server.core;

import kong.unirest.Unirest;

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
//        httpClient  = new HttpClient();
//        httpClient.start();
    }

    public void execute(String url) throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println(url);
//        httpClient.newRequest(url)
//                .timeout(60, TimeUnit.SECONDS)
//                .send(System.out::println);
        Unirest.get(url)
                .asStringAsync(System.out::println);
    }

    public static final HttpModule module()
    {
        return module_;
    }
//    private HttpClient httpClient;
    private static final HttpModule module_ = new HttpModule();
}
