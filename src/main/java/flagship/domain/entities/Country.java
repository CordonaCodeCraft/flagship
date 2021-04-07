package flagship.domain.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder

@Entity
public class Country extends BaseEntity {

    private String name;

    @OneToMany
    @Singular
    @ToString.Exclude
    private Set<Port> ports = new HashSet<>();

    @ManyToMany
    @Singular
    @ToString.Exclude
    private Set<Agent> agents = new HashSet<>();



}
