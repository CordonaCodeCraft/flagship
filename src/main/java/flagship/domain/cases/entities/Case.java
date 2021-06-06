package flagship.domain.cases.entities;

import flagship.domain.cases.entities.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

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

  @OneToMany(fetch = LAZY)
  private Set<Cargo> cargos;

  @Enumerated(value = STRING)
  private CallPurpose callPurpose;

  @OneToMany private Set<Warning> warnings;

  private Integer callCount;
  private Integer alongsideDaysExpected;
  private LocalDate estimatedDateOfArrival;
  private LocalDate estimatedDateOfDeparture;

  @Enumerated(value = STRING)
  private CaseState state;

  @OneToOne(fetch = LAZY)
  private ProformaDisbursementAccount proformaDisbursementAccount;

    public enum CaseState {}

  public enum CallPurpose {
    LOADING("Loading"),
    UNLOADING("Unloading"),
    LOADING_AND_UNLOADING("Loading and unloading"),
    RESUPPLY("Supply"),
    RECRUITMENT("Crew change"),
    POSTAL("Postal"),
    REPAIR("Repair"),
    SPECIAL_PURPOSE_PORT_VISIT("Ship repair yard");

    public final String type;

    CallPurpose(String name) {
      this.type = name;
    }
  }
}
