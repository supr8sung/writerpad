package com.xebia.fs101.writerpad.entity;

public enum WriterPadRole {
    WRITER, EDITOR, ADMIN;

    public String getRoleName() {

        return "ROLE_" + this.name();
    }
}
