package com.dev.kamran.controller;

import com.dev.kamran.dto.ApiResponse;
import com.dev.kamran.dto.PostLanguageReq;
import com.dev.kamran.entity.Language;
import com.dev.kamran.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<ApiResponse<Language>> create(@RequestBody PostLanguageReq dto) {
        Language lang = languageService.create(dto);
        return ResponseEntity.ok(ApiResponse.success(lang, HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Language>>> list() {
        List<Language> languages = languageService.getAll();
        return ResponseEntity.ok(ApiResponse.success(languages, HttpStatus.OK));
    }

    @GetMapping("/paged")
    public ResponseEntity<ApiResponse<List<Language>>> listPaged(@RequestParam int page) {
        org.springframework.data.domain.Page<Language> result = languageService.getPaged(Pageable.ofSize(page));
        return ResponseEntity.ok(ApiResponse.paginated(result, HttpStatus.OK));
    }
}

