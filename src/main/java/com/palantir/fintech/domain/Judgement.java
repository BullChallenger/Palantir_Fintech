package com.palantir.fintech.domain;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
public class Judgement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long judgementId;

    @Comment(value = "신청 ID")
    @Column(columnDefinition = "BIGINT NOT NULL")
    private Long applicationId;

    @Comment(value = "심사자")
    @Column(columnDefinition = "VARCHAR(12) NOT NULL")
    private String name;

    @Comment(value = "승인 금액")
    @Column(columnDefinition = "DECIMAL(15, 2) NOT NULL")
    private BigDecimal approvalAmount;
}
