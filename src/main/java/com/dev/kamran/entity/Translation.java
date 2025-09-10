package com.dev.kamran.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "translation", indexes = {
        @Index(name = "idx_translation_tag", columnList = "tag"),
        @Index(name = "idx_translation_base_lang", columnList = "base_language_id"),
        @Index(name = "idx_translation_trans_lang", columnList = "translated_language_id")
})
@Getter
@Setter
@NoArgsConstructor
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "base_language_id", nullable = false)
    private Language baseLanguage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "translated_language_id", nullable = false)
    private Language translatedLanguage;

    @Column(nullable = false, length = 10000)
    private String translation;

    @Column(length = 128)
    private String tag;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
