package flagship.domain.cases.entities;

import flagship.domain.cases.entities.enums.ShipType;
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
@Entity(name = "Ship")
@Table(name = "ships")
public class Ship extends BaseEntity {

  private String name;
  private String imoNumber;
  private Double lengthOverall;
  private Integer grossTonnage;

  @Enumerated(value = STRING)
  private ShipType type;
}
