package flagship.domain.entity;

import flagship.domain.entity.enums.PortArea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Port extends BaseEntity {

private String name;

@ManyToOne
private Country country;

@Enumerated(value = STRING)
private PortArea area;

}
