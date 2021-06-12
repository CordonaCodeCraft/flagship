package flagship.domain.tuples.due;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Due implements Serializable {

  private static final long serialVersionUID = 5204742248423249478L;

  private BigDecimal base;
  private BigDecimal addition;

  public Due(final BigDecimal base) {
    this.base = base;
    addition = BigDecimal.ZERO;
  }

  public Due(final double base) {
    this.base = BigDecimal.valueOf(base);
    addition = BigDecimal.ZERO;
  }

  public Due(final double base, final double addition) {
    this.base = BigDecimal.valueOf(base);
    this.addition = BigDecimal.valueOf(addition);
  }

  @Override
  @JsonValue
  public String toString() {
    return String.format("%s - %s", base, addition);
  }
}
