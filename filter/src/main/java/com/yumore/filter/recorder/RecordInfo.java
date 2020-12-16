package com.yumore.filter.recorder;

/**
 * 录制一段视频/音频的信息
 *
 * @author CainHuang
 * @date 2019/6/30
 */
public class RecordInfo {

    private final String fileName;

    private final long duration;

    private final MediaType type;

    public RecordInfo(String fileName, long duration, MediaType type) {
        this.fileName = fileName;
        this.duration = duration;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public long getDuration() {
        return duration;
    }

    public MediaType getType() {
        return type;
    }
}
