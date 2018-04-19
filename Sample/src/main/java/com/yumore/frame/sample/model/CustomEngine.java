package com.yumore.frame.sample.model;

import com.yumore.frame.library.basic.BaseEngine;

public class CustomEngine extends BaseEngine {

    private static CustomEngine api = new CustomEngine(RetrofitService.BASE_URL);

    public CustomEngine(String baseUrl) {
        super(baseUrl);
    }

    public static RetrofitService getInstance() {
        return api.getRetrofit().create(RetrofitService.class);
    }
}
