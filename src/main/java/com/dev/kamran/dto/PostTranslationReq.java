package com.dev.kamran.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostTranslationReq {
    @NotNull
    private Long baseLanguageId;

    @NotNull
    private Long translatedLanguageId;

    @NotNull
    @Size(min = 1, max = 10000)
    private String translation;
}
