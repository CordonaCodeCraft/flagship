package flagship.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Country extends BaseEntity {

    private String name;


    @OneToMany
    @Singular
    @ToString.Exclude
    private Set<Port> ports;

    @ManyToMany
    @Singular
    @ToString.Exclude
    private Set<Agent> agents;



}
