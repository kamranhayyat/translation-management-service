package com.dev.kamran.service;

import com.dev.kamran.entity.Language;
import com.dev.kamran.entity.LanguageType;
import com.dev.kamran.repository.LanguageRepository;
import com.dev.kamran.repository.TranslationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SeedFactory {

    private final LanguageRepository languageRepository;
    private final TranslationRepository translationRepository;
    private final JdbcTemplate jdbcTemplate;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;

    @Value("${app.seed.count:100000}")
    private int seedCount;

    @PostConstruct
    public void init() {
        if (!seedEnabled) return;
        if (languageRepository.count() == 0) {
            List<Language> langs = new ArrayList<>();
            langs.add(createLanguage(LanguageType.ENGLISH));
            langs.add(createLanguage(LanguageType.FRENCH));
            langs.add(createLanguage(LanguageType.SPANISH));
            langs.add(createLanguage(LanguageType.GERMAN));
            langs.add(createLanguage(LanguageType.ARABIC));
            languageRepository.saveAll(langs);
        }
        if (translationRepository.count() == 0) {
            seedTranslations();
        }
    }

    private Language createLanguage(LanguageType name) {
        Language l = new Language();
        l.setName(name);
        l.setStatus(true);
        return l;
    }

    @Transactional
    public void seedTranslations() {
        int count = seedCount;
        if (count > 200000) count = 200000;
        List<Long> langIds = jdbcTemplate.queryForList("select id from language", Long.class);
        Random rnd = new Random();
        int batch = 500;
        String sql = "insert into translation (base_language_id, translated_language_id, translation, tag, created_at, updated_at) values (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        List<Object[]> args = new ArrayList<>(batch);
        for (int i = 0; i < count; i++) {
            long base = langIds.get(rnd.nextInt(langIds.size()));
            long trans = langIds.get(rnd.nextInt(langIds.size()));
            String text = "Sample translation text number " + i;
            String tag = "tag" + (i % 50);
            args.add(new Object[]{base, trans, text, tag});
            if (args.size() == batch) {
                jdbcTemplate.batchUpdate(sql, args);
                args.clear();
            }
        }
        if (!args.isEmpty()) jdbcTemplate.batchUpdate(sql, args);
    }
}
