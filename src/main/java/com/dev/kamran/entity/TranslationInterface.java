package com.dev.kamran.entity;

public interface TranslationInterface {
    String getProvider();

    String translate(String input, String baseLangCode, String targetLangCode);
}
