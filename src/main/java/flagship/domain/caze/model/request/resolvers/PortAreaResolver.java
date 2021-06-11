package flagship.domain.caze.model.request.resolvers;

import java.util.List;

public class PortAreaResolver {

  private static final List<String> portsInSecondPortArea =
      List.of("Varna West", "Terem FA", "TEC (Power station)");

  public static PortArea resolvePortArea(final String portName) {
    return portsInSecondPortArea.contains(portName) ? PortArea.SECOND : PortArea.FIRST;
  }

  public enum PortArea {
    FIRST,
    SECOND,
    THIRD,
    FOURTH
  }
}
