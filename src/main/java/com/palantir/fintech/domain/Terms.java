package com.palantir.fintech.domain;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_deleted=false")
public class Terms extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId;

    @Comment(value = "약관")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Comment(value = "약관상세 URL")
    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String termsDetailUrl;
}
