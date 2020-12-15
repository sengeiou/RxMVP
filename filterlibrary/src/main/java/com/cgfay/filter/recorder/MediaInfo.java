package com.yumore.filter.recorder;

/**
 * 媒体信息
 *
 * @author CainHuang
 * @date 2019/7/7
 */
public class MediaInfo {

    private final String fileName;
    private final long duration;

    public MediaInfo(String name, long duration) {
        this.fileName = name;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public String getFileName() {
        return fileName;
    }
}
