package flagship.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import flagship.domain.tariffs.GtRange;
import flagship.config.serialization.RangeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addKeyDeserializer(GtRange.class, new RangeDeserializer());
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
