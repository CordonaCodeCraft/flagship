package flagship.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "flagship")
@Getter
@Setter
public class FlagshipProperties {

  public BigDecimal commission = BigDecimal.valueOf(0.3);
}
