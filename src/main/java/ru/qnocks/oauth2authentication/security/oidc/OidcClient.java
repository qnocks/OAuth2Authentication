package ru.qnocks.oauth2authentication.security.oidc;

public enum OidcClient {

    GOOGLE("google");

    OidcClient(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
