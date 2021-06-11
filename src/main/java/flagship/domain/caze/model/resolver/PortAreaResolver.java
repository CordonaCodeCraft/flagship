package flagship.domain.caze.model.resolver;

import java.util.List;

import static flagship.domain.port.entity.Port.PortArea;

public class PortAreaResolver {

  private static final List<String> portsInSecondPortArea =
      List.of("Varna West", "Terem FA", "TEC (Power station)");

  public static PortArea resolvePortArea(final String portName) {
    return portsInSecondPortArea.contains(portName) ? PortArea.SECOND : PortArea.FIRST;
  }
}
