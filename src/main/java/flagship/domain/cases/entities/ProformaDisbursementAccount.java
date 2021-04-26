package flagship.domain.cases.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "ProformaDisbursementAccount")
@Table(name = "proforma_disbursement_accounts")
public class ProformaDisbursementAccount extends BaseEntity {

  @Embedded private StateAccount stateAccount;

  @Embedded private ServiceAccount serviceAccount;

  @Embedded private AgencyAccount agencyAccount;

  @OneToMany private Set<Warning> warnings;

  private BigDecimal discount;

  private BigDecimal turnoverExpected;
}
