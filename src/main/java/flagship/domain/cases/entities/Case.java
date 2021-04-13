package flagship.domain.cases.entities;

import flagship.domain.cases.entities.enums.CallPurpose;
import flagship.domain.cases.entities.enums.CaseState;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "Case")
@Table(name = "cases")
public class Case extends BaseEntity {

  @ManyToOne(fetch = LAZY)
  private Agent agent;

  @ManyToOne(fetch = LAZY)
  private Ship ship;

  @ManyToOne(fetch = LAZY)
  private Port port;

  @Enumerated(value = STRING)
  private CallPurpose callPurpose;

  private Integer callCount;

  private Integer alongsideDaysExpected;

  @Enumerated(value = STRING)
  private CaseState state;

  @OneToOne(fetch = LAZY)
  private ProformaDisbursementAccount proformaDisbursementAccount;
}
