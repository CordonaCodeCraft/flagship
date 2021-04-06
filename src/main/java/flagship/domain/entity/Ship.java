package flagship.domain.entity;

import flagship.domain.entity.enums.ShipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class Ship extends BaseEntity {

    private String name;
    private String ImoNumber;
    private Double lengthOverall;
    private Double grossTonnage;
    private ShipType type;

}
