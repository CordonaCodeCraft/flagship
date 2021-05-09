package flagship.domain.cases.entities;

import lombok.*;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class AgencyAccount {

  private BigDecimal basicAgencyDue;
  private BigDecimal clearanceDue;
  private BigDecimal carsDue;
  private BigDecimal communicationsDue;
  // todo: expecting the business logic for calculation of the bank expenses.This is percentage of
  // something
  private BigDecimal bankExpensesDue;
  // todo: expecting the business logic for calculation of the overtime.This is percentage of the
  // basic agency fee
  private BigDecimal agencyOvertimeDue;
}
