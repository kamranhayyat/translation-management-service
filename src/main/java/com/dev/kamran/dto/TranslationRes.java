package com.dev.kamran.dto;

import com.dev.kamran.entity.Translation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRes {
    private Long id;
    private Long baseLanguageId;
    private Long translatedLanguageId;
    private String translation;
    private String tag;

    public static TranslationRes fromEntity(Translation t) {
        TranslationRes d = new TranslationRes();
        d.setId(t.getId());
        d.setBaseLanguageId(t.getBaseLanguage() != null ? t.getBaseLanguage().getId() : null);
        d.setTranslatedLanguageId(t.getTranslatedLanguage() != null ? t.getTranslatedLanguage().getId() : null);
        d.setTranslation(t.getTranslation());
        d.setTag(t.getTag());
        return d;
    }
}
