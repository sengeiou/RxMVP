package com.yumore.frame.library.request;

import android.support.annotation.NonNull;

import com.yumore.frame.library.listener.OnProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author Nathaniel
 * @date 2018/3/29-18:11
 */

public class ProgressRequestBody extends RequestBody {
    private final RequestBody requestBody;
    private final OnProgressListener onProgressListener;
    private BufferedSink bufferedSink;


    public ProgressRequestBody(RequestBody requestBody, OnProgressListener onProgressListener) {
        this.requestBody = requestBody;
        this.onProgressListener = onProgressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
        if (null == this.bufferedSink) {
            this.bufferedSink = Okio.buffer(sink(bufferedSink));
        }
        requestBody.writeTo(this.bufferedSink);
        this.bufferedSink.flush();

    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(@NonNull Buffer buffer, long byteCount) throws IOException {
                super.write(buffer, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if (onProgressListener != null) {
                    onProgressListener.onProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }
            }
        };
    }
}
