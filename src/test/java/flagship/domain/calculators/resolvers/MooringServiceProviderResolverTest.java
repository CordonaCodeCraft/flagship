package flagship.domain.calculators.resolvers;

import flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static flagship.domain.calculators.tariffs.serviceduestariffs.MooringDueTariff.MooringServiceProvider.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MooringServiceProviderResolverTest {

  @DisplayName("Should resolve mooring service provider to Lesport")
  @Test
  void testResolveMooringServiceProviderToLesport() {
    String portName = "Lesport";
    MooringServiceProvider result =
        MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(LESPORT.name());
  }

  @DisplayName("Should resolve mooring service provider to Odessos")
  @Test
  void testResolveMooringServiceProviderToOdessos() {
    String portName = "Odessos PBM";
    MooringServiceProvider result =
            MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(ODESSOS.name());
  }

  @DisplayName("Should resolve mooring service provider to Balchik")
  @Test
  void testResolveMooringServiceProviderToBalchik() {
    String portName = "Balchik port";
    MooringServiceProvider result =
            MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(BALCHIK.name());
  }

  @DisplayName("Should resolve to default mooring service provider")
  @Test
  void testResolveMooringServiceProviderToDefault() {
    String portName = "Varna West";
    MooringServiceProvider result =
            MooringServiceProviderResolver.resolveMooringServiceProvider(portName);
    assertThat(result.name()).isEqualTo(VTC.name());
  }



}
