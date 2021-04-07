package flagship.domain.entities;

import flagship.domain.entities.enums.PortArea;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder

@Entity
public class Port extends BaseEntity {

private String name;

@ManyToOne
private Country country;

@Enumerated(value = STRING)
private PortArea area;

}
