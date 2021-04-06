package flagship.domain.entity;

import flagship.domain.entity.enums.PortArea;
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

public class Port extends BaseEntity {

private String name;
private Country country;
private PortArea area;

}
