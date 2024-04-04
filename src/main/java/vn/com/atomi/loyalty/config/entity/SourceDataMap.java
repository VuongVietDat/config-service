package vn.com.atomi.loyalty.config.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.com.atomi.loyalty.base.data.BaseEntity;
import vn.com.atomi.loyalty.config.enums.*;

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
@Table(name = "CF_SOURCE_DATA_MAP")
public class SourceDataMap extends BaseEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CF_SOURCE_DATA_MAP_ID_SEQ")
  @SequenceGenerator(
      name = "CF_SOURCE_DATA_MAP_ID_SEQ",
      sequenceName = "CF_SOURCE_DATA_MAP_ID_SEQ",
      allocationSize = 1)
  private Long id;

  @Column(name = "SOURCE_ID")
  private String sourceId;

  @Column(name = "SOURCE_CODE")
  private String sourceCode;

  @Column(name = "SOURCE_NAME")
  private String sourceName;

  @Column(name = "DESTINATION_ID")
  private String destinationId;

  @Column(name = "DESTINATION_CODE")
  private String destinationCode;

  @Column(name = "SOURCE_TYPE")
  private String sourceType;

  @Column(name = "SOURCE_GROUP")
  @Enumerated(EnumType.STRING)
  private SourceGroup sourceGroup;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;
}
