package flagship.domain.entities;

import flagship.domain.entities.enums.ShipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder

@Entity
public class Ship extends BaseEntity {

    private String name;
    private String ImoNumber;
    private Double lengthOverall;
    private Double grossTonnage;

    @Enumerated(value = STRING)
    private ShipType type;

}
