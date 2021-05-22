package flagship.domain.renders.pda;

import com.itextpdf.layout.element.Cell;
import flagship.domain.cases.dto.PdaCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinancialsRender extends PdaElementsFactory {

  public Cell renderFinancials(PdaCase source) {

    return getCell();
  }
}
