package flagship.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import flagship.domain.tariffs.Due;

import java.io.IOException;
import java.io.StringWriter;

public class DueSerializer extends JsonSerializer<Due> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void serialize(
      final Due value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, value);
    gen.writeFieldName(writer.toString());
  }
}
