package flagship.domain.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder

@Entity(name = "Country")
@Table(name = "countries")
public class Country extends BaseEntity {

    private String name;

}
