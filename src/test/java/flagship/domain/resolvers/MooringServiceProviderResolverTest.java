package flagship.domain.resolvers;

import flagship.domain.calculation.tariffs.service.MooringDueTariff.MooringServiceProvider;
import flagship.domain.caze.model.resolver.MooringServiceProviderResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static flagship.domain.calculation.tariffs.service.MooringDueTariff.MooringServiceProvider.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MooringServiceProviderResolverTest {

  @DisplayName("Should resolve mooring service provider to Lesport")
  @Test
  void testResolveMooringServiceProviderToLesport() {
    final String portName = "Lesport";
    final MooringServiceProvider result =
        MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(LESPORT.name());
  }

  @DisplayName("Should resolve mooring service provider to Odessos")
  @Test
  void testResolveMooringServiceProviderToOdessos() {
    final String portName = "Odessos PBM";
    final MooringServiceProvider result =
        MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(ODESSOS.name());
  }

  @DisplayName("Should resolve mooring service provider to Balchik")
  @Test
  void testResolveMooringServiceProviderToBalchik() {
    final String portName = "Balchik port";
    final MooringServiceProvider result =
        MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(BALCHIK.name());
  }

  @DisplayName("Should resolve to default mooring service provider")
  @Test
  void testResolveMooringServiceProviderToDefault() {
    final String portName = "Varna West";
    final MooringServiceProvider result =
        MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(VTC.name());
  }
}
