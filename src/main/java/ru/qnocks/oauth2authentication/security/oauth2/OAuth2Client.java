package ru.qnocks.oauth2authentication.security.oauth2;

public enum OAuth2Client {

    GITHUB("github");

    OAuth2Client(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
