package flagship.domain.cases.entities;

import flagship.domain.calculators.tariffs.enums.PortArea;
import flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import static flagship.domain.calculators.tariffs.serviceduestariffs.PilotageDueTariff.*;
import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "Port")
@Table(name = "ports")
public class Port extends BaseEntity {

  private String name;

}
