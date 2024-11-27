package vn.com.atomi.loyalty.config.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.PointType;
import vn.com.atomi.loyalty.config.enums.Status;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CF_RULE_POC")
public class RulePOC extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_RULE_POC_ID_SEQ")
    @SequenceGenerator(name = "CF_RULE_POC_ID_SEQ", sequenceName = "CF_RULE_POC_ID_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "CODE") //Ma cong thuc
    private String code;

    @Column(name = "NAME") //Ten cong thuc
    private String name;

    @Column(name = "TYPE") //Ap dung cho loai giao dich
    private String type;

    @Column(name = "MIN_TRANSACTION") //So tien giao dich toi thieu
    private Long minTransaction;

    @Column(name = "EXCHANGE_VALUE") //So tien quy dinh
    private Long exchangeValue;

    @Column(name = "EXCHANGE_POINT") //So diem quy doi tuong ung
    private Long exchangePoint;

    @Column(name = "LIMIT_POINT_PER_TRANSACTION") //Limit diem cho giao dich
    private Long limitPointPerTransaction;

    @Column(name = "LIMIT_POINT_PER_USER") //Limit diem cho 1 user tren he thong
    private Long limitPointPerUser;

    @Column(name = "LIMIT_EVENT_PER_USER") //Limit diem cho 1 user tren 1 su kien
    private Long limitEventPerUser;

    @Column(name = "POINT_TYPE")
    @Enumerated(EnumType.STRING)
    private PointType pointType;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "CAMPAIGN_ID")
    private Long campaignId;

    @Column(name = "CAMPAIGN_CODE")
    private String campaignCode;

    @Column(name = "BUDGET_ID")
    private Long budgetId;

    @Column(name = "BUDGET_CODE")
    private String budgetCode;
}
