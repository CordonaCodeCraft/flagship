package flagship.domain;

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

    private BigDecimal clearance;
    private BigDecimal cars;
    private BigDecimal communications;
    private BigDecimal supervisor;
    private BigDecimal bankExpenses;
    private BigDecimal basicAgencyFee;
    private BigDecimal agencyOvertime;
}
