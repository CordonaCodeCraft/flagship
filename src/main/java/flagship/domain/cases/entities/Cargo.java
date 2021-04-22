package flagship.domain.cases.entities;

import flagship.domain.cases.entities.enums.CargoType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "Cargo")
@Table(name = "cargos")
public class Cargo extends BaseEntity {

    private String description;

    @Enumerated(value = STRING)
    private CargoType type;

}



