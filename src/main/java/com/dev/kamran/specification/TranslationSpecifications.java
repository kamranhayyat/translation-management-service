package com.dev.kamran.specification;

import com.dev.kamran.entity.Translation;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TranslationSpecifications {

    public static Specification<Translation> hasTagIn(List<String> tags) {
        return (root, query, cb) -> {
            Expression<String> tag = root.get("tag");
            return tag.in(tags);
        };
    }

    public static Specification<Translation> hasBaseLang(Long id) {
        return (root, query, cb) -> cb.equal(root.get("baseLanguage").get("id"), id);
    }

    public static Specification<Translation> hasTransLang(Long id) {
        return (root, query, cb) -> cb.equal(root.get("translatedLanguage").get("id"), id);
    }

    public static Specification<Translation> contentContains(String content) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("translation")), "%" + content.toLowerCase() + "%");
    }
}
