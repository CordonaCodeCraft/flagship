package flagship.domain.base.due.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import flagship.domain.base.due.tuple.Due;

import java.io.IOException;
import java.math.BigDecimal;

public class DueDeserializer extends StdDeserializer<Due> {

  public DueDeserializer() {
    this(null);
  }

  public DueDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Due deserialize(final JsonParser jp, final DeserializationContext ctxt)
      throws IOException {

    JsonNode node = jp.getCodec().readTree(jp);

    String[] pairs = node.asText().split("-");

    String left = pairs[0].trim();
    String right = pairs[1].trim();

    BigDecimal base =
        left.equals("0") ? BigDecimal.ZERO : BigDecimal.valueOf(Double.parseDouble(left));

    BigDecimal add =
        right.equals("0") ? BigDecimal.ZERO : BigDecimal.valueOf(Double.parseDouble(right));

    return new Due(base, add);
  }
}
