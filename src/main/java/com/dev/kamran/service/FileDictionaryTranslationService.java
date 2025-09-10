package com.dev.kamran.service;

import com.dev.kamran.entity.TranslationInterface;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("fileDictionaryStrategy")
public class FileDictionaryTranslationService implements TranslationInterface {

    private final Map<String, String> dictionary = new HashMap<>();

    public FileDictionaryTranslationService() {
        dictionary.put("hello", "bonjour");
        dictionary.put("world", "monde");
    }

    @Override
    public String getProvider() {
        return "file-dictionary";
    }

    @Override
    public String translate(String input, String baseLangCode, String targetLangCode) {
        String key = baseLangCode + "_" + targetLangCode + "_" + input.toLowerCase();
        return dictionary.getOrDefault(key, "[no translation]");
    }
}
