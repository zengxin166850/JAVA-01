package com.zengxin.homework0702.consts;

public enum DBTypeEnum {
    MASTER("master"), SLAVE("slave");

    private String name;

    DBTypeEnum(String name) {

    }

    public String getName() {
        return name;
    }
}
