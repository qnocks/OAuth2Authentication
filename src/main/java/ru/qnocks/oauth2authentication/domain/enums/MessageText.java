package ru.qnocks.oauth2authentication.domain.enums;

public enum MessageText {

    BRO("Bro!"),
    SIS("Sis!");

    MessageText(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}