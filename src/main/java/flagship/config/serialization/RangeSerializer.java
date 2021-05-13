package flagship.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import flagship.domain.tariffs.mix.Range;

import java.io.IOException;
import java.io.StringWriter;

public class RangeSerializer extends JsonSerializer<Range> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void serialize(Range value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    StringWriter writer = new StringWriter();
    mapper.writeValue(writer, value);
    gen.writeFieldName(writer.toString());
  }
}
