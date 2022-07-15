package com.mybudgetmanager.mybudget.settings.language;

public enum Languages {

    ENGLISH ("en"),
    ROMANIAN ("ro");

    String language;

    Languages(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return this.language;
    }
}
