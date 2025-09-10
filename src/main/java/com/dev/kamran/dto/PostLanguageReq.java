package com.dev.kamran.dto;

import com.dev.kamran.entity.LanguageType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLanguageReq {

    @NotNull(message = "Language name must not be null")
    private LanguageType name;  // enum ensures only valid values like ENGLISH, SPANISH, etc.
}
