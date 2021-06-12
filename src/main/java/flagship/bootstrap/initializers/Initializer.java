package flagship.bootstrap.initializers;

import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class Initializer {

  @Value("${flagship.easter-day-of-month}")
  protected int easterDayOfMonth;

  protected static <K, V> Map<K, V> withImmutableMap(final Map<K, V> source) {
    return Collections.unmodifiableMap(source);
  }

  protected static <T> Set<T> withImmutableSet(final Set<T> source) {
    return Collections.unmodifiableSet(source);
  }
}
