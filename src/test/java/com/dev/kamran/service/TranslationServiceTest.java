package com.dev.kamran.service;

import com.dev.kamran.dto.PostTranslationReq;
import com.dev.kamran.dto.TranslationRes;
import com.dev.kamran.entity.Language;
import com.dev.kamran.entity.LanguageType;
import com.dev.kamran.repository.LanguageRepository;
import com.dev.kamran.repository.TranslationExportTaskRepository;
import com.dev.kamran.repository.TranslationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import({TranslationService.class, SeedFactory.class})
public class TranslationServiceTest {

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private TranslationExportTaskRepository exportTaskRepository;

    @Autowired
    private TranslationService translationService;

    @BeforeEach
    public void setup() {
        Language l1 = new Language();
        l1.setName(LanguageType.ENGLISH);
        l1.setStatus(true);
        Language l2 = new Language();
        l2.setName(LanguageType.FRENCH);
        l2.setStatus(true);
        languageRepository.save(l1);
        languageRepository.save(l2);
    }

    @Test
    public void testCreateTranslation() {
        Long baseId = languageRepository.findByName(LanguageType.ENGLISH).get().getId();
        Long transId = languageRepository.findByName(LanguageType.FRENCH).get().getId();

        PostTranslationReq dto = new PostTranslationReq();
        dto.setBaseLanguageId(baseId);
        dto.setTranslatedLanguageId(transId);
        dto.setTranslation("Hello world");

        TranslationRes created = translationService.create(dto, "tag1", "file-dictionary");
        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals("tag1", created.getTag());
    }

    @Test
    public void testSearchByContent() {
        Long baseId = languageRepository.findByName(LanguageType.ENGLISH).get().getId();
        Long transId = languageRepository.findByName(LanguageType.FRENCH).get().getId();

        PostTranslationReq dto = new PostTranslationReq();
        dto.setBaseLanguageId(baseId);
        dto.setTranslatedLanguageId(transId);
        dto.setTranslation("UniqueContentXYZ");

        translationService.create(dto, "searchTag", "file-dictionary");

        var page = translationService.search(Optional.ofNullable(null), Optional.of(baseId), Optional.of(transId), Optional.of("UniqueContentXYZ"), org.springframework.data.domain.Pageable.unpaged());
        Assertions.assertTrue(page.getTotalElements() >= 1);
    }
}
