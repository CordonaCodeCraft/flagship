package flagship.domain.tuples.range.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import flagship.domain.tuples.range.Range;

import java.io.IOException;
import java.io.StringWriter;

public class RangeSerializer extends JsonSerializer<Range> {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void serialize(final Range value, final JsonGenerator gen, final SerializerProvider serializers)
      throws IOException {
    final StringWriter writer = new StringWriter();
    mapper.writeValue(writer, value);
    gen.writeFieldName(writer.toString());
  }
}
