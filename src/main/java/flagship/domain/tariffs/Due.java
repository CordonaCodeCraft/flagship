package flagship.domain.tariffs;

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

  public Due(BigDecimal base) {
    this.base = base;
  }

  public Due(double base) {
    this.base = BigDecimal.valueOf(base);
  }

  public Due(double base, double addition) {
    this.base = BigDecimal.valueOf(base);
    this.addition = BigDecimal.valueOf(addition);
  }

  @Override
  @JsonValue
  public String toString() {
    return String.format("%s - %s", base, addition);
  }
}
