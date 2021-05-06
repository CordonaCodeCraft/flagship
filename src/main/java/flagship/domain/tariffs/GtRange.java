package flagship.domain.tariffs;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class GtRange implements Serializable {

  private static final long serialVersionUID = 5204742248423249478L;

  private Integer min;
  private Integer max;

  @Override
  @JsonValue
  public String toString() {
    return String.format("%s - %s", min, max);
  }
}
