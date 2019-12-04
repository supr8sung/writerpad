package com.xebia.fs101.writerpad.response;

import com.xebia.fs101.writerpad.model.ReadingTime;

public class ReadingTimeResponse {
    private String slugId;
    private ReadingTime readingTime;

    public ReadingTimeResponse(String slugId, ReadingTime readingTime) {

        this.slugId = slugId;
        this.readingTime = readingTime;
    }

    public String getSlugId() {

        return slugId;
    }

    public ReadingTime getReadingTime() {

        return readingTime;
    }
}
