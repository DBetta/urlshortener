package com.dbetta.urlshortener.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author Denis Gitonga
 */
@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(schema = "url_shortener", name = "url_mapping")
public class UrlMapping {
    @Id
    private String shortUrl;
    private String longUrl;
    private LocalDateTime expiresAt;
    private int visits;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    private long version;
}
