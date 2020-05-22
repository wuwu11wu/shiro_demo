package com.zhi.shiro.component;

public enum LoginType {
    ADMIN("Admin"),
    USER("User");

    private String type;

    private LoginType(String type) {
        this.type = type;
    }

}
