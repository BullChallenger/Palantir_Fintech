package com.palantir.fintech.domain;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_deleted=false")
public class Application extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Comment(value = "신청자")
    @Column(columnDefinition = "VARCHAR(12) DEFAULT NULL COMMENT '신청자'")
    private String name;

    @Comment(value = "전화번호")
    @Column(columnDefinition = "VARCHAR(13) DEFAULT NULL")
    private String cellPhone;

    @Comment(value = "신청자 이메일")
    @Column(columnDefinition = "VARCHAR(50) DEFAULT NULL")
    private String email;

    @Comment(value = "금리")
    @Column(columnDefinition = "DECIMAL(5, 4) DEFAULT NULL")
    private BigDecimal interestRate;

    @Comment(value = "취급수수료")
    @Column(columnDefinition = "DECIMAL(5, 4) DEFAULT NULL")
    private BigDecimal fee;

    @Comment(value = "만기")
    @Column(columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime maturity;

    @Comment(value = "대출 신청 금액")
    @Column(columnDefinition = "DECIMAL(15, 2) DEFAULT NULL")
    private BigDecimal hopeAmount;

    @Comment(value = "승인 금액")
    @Column(columnDefinition = "DECIMAL(15, 2) DEFAULT NULL")
    private BigDecimal approvalAmount;

    @Comment(value = "신청일자")
    @Column(columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime appliedAt;

    @Comment(value = "계약일자")
    @Column(columnDefinition = "DATETIME DEFAULT NULL")
    private LocalDateTime contractedAt;
}
