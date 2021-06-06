package flagship.domain.cases.entities;

import flagship.domain.cases.entities.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

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
