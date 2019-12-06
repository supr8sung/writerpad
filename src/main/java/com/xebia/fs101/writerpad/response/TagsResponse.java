package com.xebia.fs101.writerpad.response;

import java.math.BigInteger;

public class TagsResponse {
    String tag;
    BigInteger occurence;

    public String getTag() {
        return tag;
    }

    public BigInteger getOccurence() {
        return occurence;
    }

    public TagsResponse(String tag, BigInteger occurence) {

        this.tag = tag;
        this.occurence = occurence;
    }
}
