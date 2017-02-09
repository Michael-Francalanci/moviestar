package com.francalanci.michael.moviestars;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by michael on 06/02/17.
 */

public class TheMovieAPI {

    public static String HOST, API_KEY;

    protected OkHttpClient client;
    private HttpUrl.Builder urlBuilder;

    public TheMovieAPI() {
        HOST = BuildConfig.THEMOVIEDB_API_HOST;
        API_KEY = BuildConfig.THEMOVIEDB_API_KEY;
        OkHttpClient.Builder builderClient = new OkHttpClient.Builder();
        client = builderClient.build();
    }

    private Request createRequest(String host, String path_request) {
        urlBuilder = HttpUrl.parse(host ).newBuilder();
        urlBuilder.addPathSegment(path_request);
        urlBuilder.addQueryParameter("api_key", API_KEY);
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }

    public Call getTopRated() {
        Request request = createRequest(HOST, "top_rated");
        return client.newCall(request);
    }

    public Call getPopular() {
        Request request = createRequest(HOST, "popular");
        return client.newCall(request);
    }
}
