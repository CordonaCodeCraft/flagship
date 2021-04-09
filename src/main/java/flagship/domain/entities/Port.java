package flagship.domain.entities;

import flagship.domain.entities.enums.PortArea;
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

@Entity(name = "Port")
@Table(name = "ports")
public class Port extends BaseEntity {

private String name;

@Enumerated(value = STRING)
private PortArea area;

}
