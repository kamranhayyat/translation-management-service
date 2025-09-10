package com.dev.kamran.service;

import com.dev.kamran.dto.ExportTaskReq;
import com.dev.kamran.dto.PostTranslationReq;
import com.dev.kamran.dto.TranslationRes;
import com.dev.kamran.entity.Language;
import com.dev.kamran.entity.Translation;
import com.dev.kamran.entity.TranslationExportTask;
import com.dev.kamran.entity.TranslationInterface;
import com.dev.kamran.repository.LanguageRepository;
import com.dev.kamran.repository.TranslationExportTaskRepository;
import com.dev.kamran.repository.TranslationRepository;
import com.dev.kamran.specification.TranslationSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final TranslationExportTaskRepository exportTaskRepository;
    private final LanguageRepository languageRepository;
    private final Map<String, TranslationInterface> strategies;

    public TranslationRes create(PostTranslationReq req, String tag, String provider) {
        Language base = languageRepository.findById(req.getBaseLanguageId())
                .orElseThrow(() -> new IllegalArgumentException("Base language not found"));

        Language target = languageRepository.findById(req.getTranslatedLanguageId())
                .orElseThrow(() -> new IllegalArgumentException("Translated language not found"));

        TranslationInterface dictionaryProvider = strategies.values().stream()
                .filter(s -> s.getProvider().equals(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provider not found: " + provider));

        String translatedText = dictionaryProvider.translate(req.getTranslation(), base.getName().name(), target.getName().name());

        Translation translation = new Translation();
        translation.setBaseLanguage(base);
        translation.setTranslatedLanguage(target);
        translation.setTranslation(translatedText);
        translation.setTag(tag);

        return TranslationRes.fromEntity(translationRepository.save(translation));
    }

    @Transactional(readOnly = true)
    public Optional<TranslationRes> getById(Long id) {
        return translationRepository.findById(id).map(TranslationRes::fromEntity);
    }

    public TranslationRes updateText(Long id, String newText) {
        Translation t = translationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Translation not found"));
        t.setTranslation(newText);
        return TranslationRes.fromEntity(translationRepository.save(t));
    }

    public void delete(Long id) {
        if (!translationRepository.existsById(id)) {
            throw new IllegalArgumentException("Translation not found");
        }
        translationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TranslationRes> search(Optional<String> tags,
                                       Optional<Long> baseLang,
                                       Optional<Long> transLang,
                                       Optional<String> content,
                                       Pageable pageable) {
        Specification<Translation> spec = Specification.where(null);

        if (tags.isPresent()) {
            List<String> tagList = Arrays.stream(tags.get().split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (!tagList.isEmpty()) spec = spec.and(TranslationSpecifications.hasTagIn(tagList));
        }
        if (baseLang.isPresent()) {
            spec = spec.and(TranslationSpecifications.hasBaseLang(baseLang.get()));
        }
        if (transLang.isPresent()) {
            spec = spec.and(TranslationSpecifications.hasTransLang(transLang.get()));
        }
        if (content.isPresent()) {
            spec = spec.and(TranslationSpecifications.contentContains(content.get()));
        }

        return translationRepository.findAll(spec, pageable)
                .map(TranslationRes::fromEntity);
    }

    public ExportTaskReq createExportTask(String userId) {
        TranslationExportTask task = new TranslationExportTask();
        task.setUserId(userId);
        task.setStatus("PENDING");
        TranslationExportTask saved = exportTaskRepository.save(task);

        processExportAsync(saved.getId());

        return ExportTaskReq.fromEntity(saved);
    }

    public ExportTaskReq getExportTask(Long id) {
        TranslationExportTask task = exportTaskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return ExportTaskReq.fromEntity(task);
    }

    @Async
    public void processExportAsync(Long taskId) {
        TranslationExportTask task = exportTaskRepository.findById(taskId).orElse(null);
        if (task == null) return;
        try {
            task.setStatus("PROCESSING");
            exportTaskRepository.save(task);

            String fileName = Paths.get(System.getProperty("java.io.tmpdir"),
                    "translations_" + taskId + ".csv").toString();

            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
                // header
                pw.println("id,baseLanguageId,translatedLanguageId,tag,translation,createdAt");
                translationRepository.streamAll().forEach(t -> {
                    try {
                        String line = String.format("%d,%d,%d,%s,%s,%s",
                                t.getId(),
                                t.getBaseLanguage() != null ? t.getBaseLanguage().getId() : -1,
                                t.getTranslatedLanguage() != null ? t.getTranslatedLanguage().getId() : -1,
                                t.getTag() != null ? escape(t.getTag()) : "",
                                escape(t.getTranslation()),
                                t.getCreatedAt() != null ? t.getCreatedAt().toString() : ""
                        );
                        pw.println(line);
                    } catch (Exception ignored) {
                    }
                });
            }

            task.setResultUrl("file://" + fileName);
            task.setStatus("DONE");
            exportTaskRepository.save(task);

        } catch (Exception ex) {
            task.setStatus("FAILED");
            exportTaskRepository.save(task);
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replaceAll("\"", "\"").replaceAll("\r|\n", " ").replaceAll(",", " ");
    }
}
