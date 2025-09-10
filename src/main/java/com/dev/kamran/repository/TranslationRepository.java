package com.dev.kamran.repository;

import com.dev.kamran.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

public interface TranslationRepository extends JpaRepository<Translation, Long>, JpaSpecificationExecutor<Translation> {

    @Transactional(readOnly = true)
    @Query("select t from Translation t")
    Stream<Translation> streamAll();
}
