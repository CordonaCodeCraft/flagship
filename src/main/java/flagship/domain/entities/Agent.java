package flagship.domain.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@SuperBuilder

@Entity
public class Agent extends BaseEntity {

    private String name;

    @ManyToMany
    @Singular
    @ToString.Exclude
    private Set<Country> countries = new HashSet<>();




}
