package com.yumore.common.callback;

import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author Nathaniel
 * @date 2019/5/20 - 19:17
 */
public interface MultiplyCallback {

    void onFailure(Call call, IOException e, int position);

    void onResponse(Call call, Response response, int position) throws IOException;
}
