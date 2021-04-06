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
public class StateAccount {

    private BigDecimal tonnageDue;
    private BigDecimal wharfDue;
    private BigDecimal canalDue;
    private BigDecimal lightDue;
    private BigDecimal sailingPermission;
    private BigDecimal pilotage;
    private BigDecimal tugService;
    private BigDecimal mooringAndUnmooring;
    private BigDecimal marpolDue;

}
