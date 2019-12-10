package com.xebia.fs101.writerpad.response;

public class TagsResponse {
    private String tag;
    private Long occurence;

    public TagsResponse(String tag, Long occurence) {

        this.tag = tag;
        this.occurence = occurence;
    }

    public String getTag() {

        return tag;
    }

    public Long getOccurence() {

        return occurence;
    }
}
