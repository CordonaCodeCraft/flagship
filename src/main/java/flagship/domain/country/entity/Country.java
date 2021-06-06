package flagship.domain.country.entity;

import flagship.domain.base.entity.BaseEntity;
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
@Entity(name = "Country")
@Table(name = "countries")
public class Country extends BaseEntity {

  private String name;
}
