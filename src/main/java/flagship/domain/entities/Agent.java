package flagship.domain.entities;

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

@Entity(name = "Agent")
@Table(name = "agents")
public class Agent extends BaseEntity {

    private String name;

}
