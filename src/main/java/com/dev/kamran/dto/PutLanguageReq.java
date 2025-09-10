package com.dev.kamran.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutLanguageReq {
    @NotNull
    private Boolean status;
}
