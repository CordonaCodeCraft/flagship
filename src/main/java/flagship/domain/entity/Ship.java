package flagship.domain.entity;

import flagship.domain.entity.enums.ShipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
