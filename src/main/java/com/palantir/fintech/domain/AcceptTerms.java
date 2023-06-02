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
public class AcceptTerms extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acceptTermsId;

    @Comment(value = "신청 ID")
    @Column(columnDefinition = "BIGINT NOT NULL")
    private Long applicationId;

    @Comment(value = "약관 ID")
    @Column(columnDefinition = "BIGINT NOT NULL")
    private Long termsId;
}
