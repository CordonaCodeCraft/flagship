package flagship.domain.entity;

import flagship.domain.AgencyAccount;
import flagship.domain.StateAccount;
import flagship.domain.entity.enums.CallPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class ProformaDisbursementAccount extends BaseEntity {

    private Agent agent;
    private Ship ship;
    private Port port;
    private Integer callCount;
    private CallPurpose callPurpose;
    private Integer alongsideDaysExpected;
    private AgencyAccount agencyAccount;
    private StateAccount stateAccount;
    private BigDecimal turnoverExpected;
    private BigDecimal discount;

}
