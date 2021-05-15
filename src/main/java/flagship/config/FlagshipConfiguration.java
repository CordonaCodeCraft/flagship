package flagship.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.math.BigDecimal;

@ConstructorBinding
@ConfigurationProperties("flagship")
@Getter
@RequiredArgsConstructor
public class FlagshipConfiguration {
  private final BigDecimal commissionCoefficient;
}
