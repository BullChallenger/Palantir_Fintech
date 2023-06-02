package com.palantir.fintech.domain;

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
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_deleted=false")
public class Entry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long entryId;

    @Comment("신청 ID")
    @Column(columnDefinition = "NOT NULL")
    private Long applicationId;

    @Comment("집행 금액")
    @Column(columnDefinition = "DECIMAL(15, 2) NOT NULL")
    private BigDecimal entryAmount;
}
