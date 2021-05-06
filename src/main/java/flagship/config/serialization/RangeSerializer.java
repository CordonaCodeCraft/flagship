package flagship.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import flagship.domain.tariffs.GtRange;

import java.io.IOException;
import java.io.StringWriter;

public class RangeSerializer extends JsonSerializer<GtRange> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void serialize(GtRange value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, value);
    gen.writeFieldName(writer.toString());
  }
}
