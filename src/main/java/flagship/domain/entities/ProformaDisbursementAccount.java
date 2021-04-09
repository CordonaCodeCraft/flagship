package flagship.domain.entities;

import flagship.domain.AgencyAccount;
import flagship.domain.StateAccount;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
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

    @Embedded
    private AgencyAccount agencyAccount;

    @Embedded
    private StateAccount stateAccount;

    private BigDecimal discount;

    private BigDecimal turnoverExpected;

}
