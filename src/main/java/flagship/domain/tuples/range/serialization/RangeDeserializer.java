package flagship.domain.tuples.range.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import flagship.domain.tuples.range.Range;

public class RangeDeserializer extends KeyDeserializer {

  @Override
  public Object deserializeKey(final String key, final DeserializationContext ctxt) {
    final String[] pairs = key.split("-");
    final Integer min = Integer.valueOf(pairs[0].trim());
    final Integer max = Integer.valueOf(pairs[1].trim());
    return new Range(min, max);
  }
}
