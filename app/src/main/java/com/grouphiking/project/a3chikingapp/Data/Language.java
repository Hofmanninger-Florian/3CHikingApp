package com.grouphiking.project.a3chikingapp.Data;

import java.util.Locale;

public enum Language {
    GERMAN("de"),
    ENGLISH(Locale.ROOT.toString());

    private String Language;

    private Language(String lang){
        this.Language = lang;
    }

    public String getLanguage() {
        return Language;
    }
}
