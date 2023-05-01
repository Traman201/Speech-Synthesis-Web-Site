package com.sergeev.srp.common.model.enums;

import lombok.Getter;

@Getter
public enum Systems {
    MaryTTS("MaryTTS"),
    HMM("HMM");

    private final String name;

    Systems(String name) {
        this.name = name;
    }
}
