package flagship.domain.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
public class Agent extends BaseEntity {

    private String name;

    @ManyToMany
    @Singular
    @ToString.Exclude
    private Set<Country> countries;




}
