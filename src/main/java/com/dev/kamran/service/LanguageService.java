package com.dev.kamran.service;

import com.dev.kamran.dto.PostLanguageReq;
import com.dev.kamran.dto.PutLanguageReq;
import com.dev.kamran.entity.Language;
import com.dev.kamran.entity.LanguageType;
import com.dev.kamran.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository repo;

    public Language create(PostLanguageReq dto) {
        LanguageType langEnum = dto.getName();

        Language lang = new Language();
        lang.setName(langEnum);
        lang.setCode(langEnum.getCode());
        lang.setStatus(true);

        return repo.save(lang);
    }

    public Language updateStatus(Long id, PutLanguageReq dto) {
        Language lang = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Language not found"));
        lang.setStatus(dto.getStatus());
        return repo.save(lang);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Language> getAll() {
        return repo.findAll();
    }

    public Page<Language> getPaged(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
