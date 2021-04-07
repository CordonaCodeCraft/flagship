package flagship.domain.entities;

import flagship.domain.entities.enums.CaseState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder

@Entity
public class Case extends BaseEntity {

    @OneToOne
    private ProformaDisbursementAccount proformaDisbursementAccount;

    @Enumerated(STRING)
    private CaseState caseState;

}
