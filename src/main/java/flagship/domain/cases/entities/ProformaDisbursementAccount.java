package flagship.domain.cases.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "ProformaDisbursementAccount")
@Table(name = "proforma_disbursement_accounts")
public class ProformaDisbursementAccount extends BaseEntity {

  private BigDecimal tonnageDue;
  private BigDecimal wharfDue;
  private BigDecimal canalDue;
  private BigDecimal lightDue;
  private BigDecimal marpolDue;
  private BigDecimal mooringDue;
  private BigDecimal boomContainmentDue;
  private BigDecimal sailingPermissionDue;
  private BigDecimal pilotageDue;
  private BigDecimal tugDue;
  private BigDecimal basicAgencyDue;
  private BigDecimal carsDue;
  private BigDecimal clearanceDue;
  private BigDecimal communicationsDue;
  private BigDecimal bankExpensesDue;
  private BigDecimal overtimeDue;
  private BigDecimal clientDiscount;
  private BigDecimal payableTotal;
  private BigDecimal totalAfterDiscount;
  private BigDecimal profitExpected;
}
