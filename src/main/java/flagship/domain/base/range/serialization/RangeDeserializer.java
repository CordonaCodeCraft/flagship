package flagship.domain.base.range.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import flagship.domain.base.range.tuple.Range;

public class RangeDeserializer extends KeyDeserializer {

  @Override
  public Object deserializeKey(String key, DeserializationContext ctxt) {
    String[] pairs = key.split("-");
    Integer min = Integer.valueOf(pairs[0].trim());
    Integer max = Integer.valueOf(pairs[1].trim());
    return new Range(min, max);
  }
}
