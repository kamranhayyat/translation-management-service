package com.dev.kamran.controller;

import com.dev.kamran.dto.ExportTaskReq;
import com.dev.kamran.dto.PostTranslationReq;
import com.dev.kamran.dto.TranslationRes;
import com.dev.kamran.dto.PutTranslationReq;
import com.dev.kamran.dto.ApiResponse;
import com.dev.kamran.service.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/translations")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping
    public ResponseEntity<ApiResponse<TranslationRes>> create(
            @Valid @RequestBody PostTranslationReq dto,
            @RequestHeader(value = "X-Translation-Tag", required = false) String tag,
            @RequestParam(defaultValue = "file-dictionary") String strategy
    ) {
        return ResponseEntity.status(201)
                .body(ApiResponse.success(translationService.create(dto, tag, strategy), HttpStatus.OK));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TranslationRes>> update(
            @PathVariable Long id,
            @Valid @RequestBody PutTranslationReq dto) {

        TranslationRes result = translationService.updateText(id, dto.getTranslation());
        return ResponseEntity.ok(ApiResponse.success(result, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TranslationRes>> get(@PathVariable Long id) {
        Optional<TranslationRes> result = translationService.getById(id);
        return result.map(r -> ResponseEntity.ok(ApiResponse.success(r, HttpStatus.OK)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Translation not found", HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        translationService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, HttpStatus.NO_CONTENT));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TranslationRes>>> search(
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) Long baseLang,
            @RequestParam(required = false) Long transLang,
            @RequestParam(required = false) String content,
            Pageable pageable) {

        Page<TranslationRes> result = translationService.search(
                Optional.ofNullable(tags),
                Optional.ofNullable(baseLang),
                Optional.ofNullable(transLang),
                Optional.ofNullable(content),
                pageable
        );

        return ResponseEntity.ok(ApiResponse.paginated(result, HttpStatus.OK));
    }

    @PostMapping("/export")
    public ResponseEntity<ApiResponse<ExportTaskReq>> export(
            @RequestParam(required = false) String userId) {

        ExportTaskReq task = translationService.createExportTask(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(task, HttpStatus.ACCEPTED));
    }

    @GetMapping("/export/{id}")
    public ResponseEntity<ApiResponse<ExportTaskReq>> getExport(@PathVariable Long id) {
        ExportTaskReq task = translationService.getExportTask(id);
        return ResponseEntity.ok(ApiResponse.success(task, HttpStatus.OK));
    }
}
