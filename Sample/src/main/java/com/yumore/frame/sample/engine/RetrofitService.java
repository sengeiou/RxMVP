package com.yumore.frame.sample.engine;


import com.yumore.frame.sample.entity.Test;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RetrofitService {
    String BASE_URL = "https://news-at.zhihu.com/api/4/";

    @GET("news/latest")
    Observable<Test> test();
}