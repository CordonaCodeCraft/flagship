package flagship;

import flagship.config.FlagshipConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FlagshipConfiguration.class)
public class FlagshipApplication {
  public static void main(String[] args) {
    SpringApplication.run(FlagshipApplication.class, args);
  }
}
