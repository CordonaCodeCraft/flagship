package flagship.domain.tuples.due.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import flagship.domain.tuples.due.Due;

import java.io.IOException;
import java.math.BigDecimal;

public class DueDeserializer extends StdDeserializer<Due> {

  public DueDeserializer() {
    this(null);
  }

  public DueDeserializer(final Class<?> vc) {
    super(vc);
  }

  @Override
  public Due deserialize(final JsonParser jp, final DeserializationContext ctxt)
      throws IOException {

    final JsonNode node = jp.getCodec().readTree(jp);

    final String[] pairs = node.asText().split("-");

    final String left = pairs[0].trim();
    final String right = pairs[1].trim();

    final BigDecimal base =
        left.equals("0") ? BigDecimal.ZERO : BigDecimal.valueOf(Double.parseDouble(left));

    final BigDecimal add =
        right.equals("0") ? BigDecimal.ZERO : BigDecimal.valueOf(Double.parseDouble(right));

    return new Due(base, add);
  }
}
