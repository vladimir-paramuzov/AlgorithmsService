package ru.unn.webservice.storage;

public enum Language {
    CPP("CPP");

    private String lang;

    Language(final String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return lang;
    }
}
