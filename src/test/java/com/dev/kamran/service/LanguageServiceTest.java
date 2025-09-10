package com.dev.kamran.service;

import com.dev.kamran.dto.PostLanguageReq;
import com.dev.kamran.dto.PutLanguageReq;
import com.dev.kamran.entity.Language;
import com.dev.kamran.entity.LanguageType;
import com.dev.kamran.repository.LanguageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.dev.kamran.entity.LanguageType.ENGLISH;

@DataJpaTest
@Import(LanguageService.class)
public class LanguageServiceTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    @Test
    public void testCreateAndList() {
        PostLanguageReq dto = new PostLanguageReq();
        dto.setName(ENGLISH);
        Language created = languageService.create(dto);
        Assertions.assertNotNull(created.getId());

        List<Language> all = languageService.getAll();
        Assertions.assertTrue(all.stream().anyMatch(l -> ENGLISH.equals(l.getName())));
    }

    @Test
    public void testUpdateStatus() {
        PostLanguageReq dto = new PostLanguageReq();
        dto.setName(LanguageType.ARABIC);
        Language created = languageService.create(dto);

        PutLanguageReq upd = new PutLanguageReq();
        upd.setStatus(false);
        Language updated = languageService.updateStatus(created.getId(), upd);
        Assertions.assertFalse(updated.getStatus());
    }
}
