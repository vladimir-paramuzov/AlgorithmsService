package ru.unn.webservice.infrastructure;

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
