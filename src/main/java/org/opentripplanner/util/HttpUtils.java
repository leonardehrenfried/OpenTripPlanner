package org.opentripplanner.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtils {
    
    private static final long TIMEOUT_CONNECTION = 5000;

    public static InputStream getData(URI uri) throws IOException {
        return getData(uri, null);
    }

    public static InputStream getData(String uri) throws IOException {
        return getData(URI.create(uri));
    }

    public static InputStream getData(String uri, Map<String, String> headers) throws IOException {
        return getData(URI.create(uri), headers);
    }

    public static InputStream getData(
        URI uri, long timeout, Map<String, String> requestHeaderValues
    ) throws IOException {
        HttpGet httpget = new HttpGet(uri);
        if (requestHeaderValues != null) {
            for (Map.Entry<String, String> entry : requestHeaderValues.entrySet()) {
                httpget.addHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpClient httpclient = getClient(timeout, timeout);
        HttpResponse response = httpclient.execute(httpget);
        if(response.getStatusLine().getStatusCode() != 200) {
            return null;
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        return entity.getContent();
    }

    public static InputStream getData(URI uri, Map<String, String> requestHeaderValues) throws IOException {
        return getData(uri, TIMEOUT_CONNECTION, requestHeaderValues);
    }

    private static HttpClient getClient(long timeoutConnection, long timeoutSocket) {
        return HttpClientBuilder.create()
                .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout((int)timeoutSocket).build())
                .setConnectionTimeToLive(timeoutConnection, TimeUnit.MILLISECONDS)
                .build();
    }

    public static InputStream openInputStream(String url, Map<String, String> headers) throws IOException {
        return openInputStream(URI.create(url), headers);
    }
    public static InputStream openInputStream(URI uri, Map<String, String> headers) throws IOException {
        URL downloadUrl = uri.toURL();
        String proto = downloadUrl.getProtocol();
        if (proto.equals("http") || proto.equals("https")) {
            return HttpUtils.getData(uri, headers);
        } else {
            // Local file probably, try standard java
            return downloadUrl.openStream();
        }
    }

}
