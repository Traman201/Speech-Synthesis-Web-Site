package com.sergeev.srp.common.security;

import lombok.Getter;

@Getter
public enum Roles {
    SITE("Site");


    final String name;

    Roles(String name) {
        this.name = name;
    }

}
