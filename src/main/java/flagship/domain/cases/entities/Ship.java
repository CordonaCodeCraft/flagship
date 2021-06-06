package flagship.domain.cases.entities;

import flagship.domain.cases.entities.base.BaseEntity;
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

  @Enumerated(value = STRING)
  private ShipType type;

  private String name;
  private String imoNumber;
  private Double lengthOverall;
  private Integer grossTonnage;

    public enum ShipType {
      BULK_CARRIER("Bulk carrier"),
      REEFER("Reefer vessel"),
      CONTAINER("Container vessel"),
      PASSENGER("Passenger vessel"),
      RECREATIONAL("Recreational vessel"),
      OIL_TANKER("Oil tanker"),
      NAVY("Navy vessel"),
      WORK_SHIP("Work ship");

      public final String type;

      ShipType(String name) {
        this.type = name;
      }
    }
}
