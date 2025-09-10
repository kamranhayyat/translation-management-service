package com.dev.kamran.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutTranslationReq {
    @NotNull
    @Size(min = 1, max = 10000)
    private String translation;
}
