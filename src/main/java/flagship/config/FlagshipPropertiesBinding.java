package flagship.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.math.BigDecimal;

@ConstructorBinding
@Getter
@RequiredArgsConstructor
public class FlagshipPropertiesBinding {
  private final BigDecimal commissionCoefficient;
  private final Boolean newInstallation;
}
