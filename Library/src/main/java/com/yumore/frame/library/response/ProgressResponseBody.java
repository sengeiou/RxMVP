package com.yumore.frame.library.response;

import com.yumore.frame.library.listener.OnProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author Nathaniel
 * @date 2018/3/29-18:12
 */

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final OnProgressListener onProgressListener;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, OnProgressListener onProgressListener) {
        this.responseBody = responseBody;
        this.onProgressListener = onProgressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (onProgressListener != null) {
                    onProgressListener.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };
    }
}
