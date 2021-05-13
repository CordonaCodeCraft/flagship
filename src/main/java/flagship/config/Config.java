package flagship.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import flagship.bootstrap.StateDuesTariffsInitializer;
import flagship.config.serialization.DueDeserializer;
import flagship.config.serialization.RangeDeserializer;
import flagship.domain.TariffsFactory;
import flagship.domain.tariffs.Tariff;
import flagship.domain.tariffs.mix.Due;
import flagship.domain.tariffs.mix.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

import static flagship.domain.calculators.DueCalculator.CalculatorType;
import static flagship.domain.calculators.DueCalculator.CalculatorType.TONNAGE_DUE_CALCULATOR;

@Configuration
public class Config {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(Range.class, new RangeDeserializer());
    simpleModule.addDeserializer(Due.class, new DueDeserializer());
    objectMapper.registerModule(simpleModule);
    return objectMapper;
  }

  //    @Bean
  //    @Primary
  //    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
  //        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
  //        objectMapper.registerModule(new JavaTimeModule());
  //        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  //        objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS,
  // false);
  //        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  //        return objectMapper;
  //    }
}
