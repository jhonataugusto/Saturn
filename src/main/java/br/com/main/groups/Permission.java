package br.com.main.groups;

import lombok.Getter;

public enum Permission {
    ADMIN("ADMIN"),
    MOD("MOD"),
    TRIAL("TRIAL"),
    HELPER("HELPER"),
    YOUTUBER_PLUS("YOUTUBER_PLUS"),
    YOUTUBER("YOUTUBER"),
    VIP("VIP"),
    MEMBER("MEMBER");

    @Getter
    private String description;

    Permission(String description){
        this.description = description;

    }
}
