package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.Status;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CF_RANK")
public class Rank extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_RANK_ID_SEQ")
  @SequenceGenerator(name = "CF_RANK_ID_SEQ", sequenceName = "CF_RANK_ID_SEQ", allocationSize = 1)
  private Long id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ORDER_NO")
  private int orderNo;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
