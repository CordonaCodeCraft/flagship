package flagship.domain.entity;

import flagship.domain.AgencyAccount;
import flagship.domain.StateAccount;
import flagship.domain.entity.enums.CallPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class ProformaDisbursementAccount extends BaseEntity {

    @ManyToOne
    private Agent agent;

    @ManyToOne
    private Ship ship;

    @ManyToOne
    private Port port;

    private Integer callCount;

    @Enumerated(value = STRING)
    private CallPurpose callPurpose;

    private Integer alongsideDaysExpected;

    @Embedded
    private AgencyAccount agencyAccount;

    @Embedded
    private StateAccount stateAccount;

    private BigDecimal discount;

    private BigDecimal turnoverExpected;


}
