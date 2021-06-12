package flagship.bootstrap.initializers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class Initializer {

  protected static <K, V> Map<K, V> withImmutableMap(final Map<K, V> source) {
    return Collections.unmodifiableMap(source);
  }

  protected static <T> Set<T> withImmutableSet(final Set<T> source) {
    return Collections.unmodifiableSet(source);
  }
}
