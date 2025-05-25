package com.michael.provider;

import com.google.gson.Gson;

public class GSONProvider {
    private static final Gson gson = new Gson();
    private GSONProvider() {

    }

    public static Gson getGson() {
        return gson;
    }
}
