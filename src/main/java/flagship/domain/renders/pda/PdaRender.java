package flagship.domain.renders.pda;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.renders.pda.elements.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PdaRender extends PdaElementsFactory {

  private final PdaCase source;

  public void renderPdaDocument() throws IOException {

    final String filePath = "D:/Gdrive/Inbox/" + getFileName(source);

    final Document pda = getNewDocument(filePath);

    final Table masterTable = getOuterTable();

    masterTable.addCell(LogoRender.renderLogo());
    masterTable.addCell(TitleRender.renderTitle());
    masterTable.addCell(ShipParticularsRender.renderShipParticulars(source));
    masterTable.addCell(CompanyDetailsRender.renderCompanyDetails(source));
    masterTable.addCell(CallDetailsRender.renderCallDetails(source));
    masterTable.addCell(PaymentDetailsRender.renderPaymentDetails(source));
    masterTable.addCell(FinancialsRender.renderFinancials(source));

    pda.add(masterTable);

    pda.close();
  }

  private String getFileName(final PdaCase source) {
    return String.format("%s_%s.pdf", source.getShip().getName(), LocalDate.now().toString());
  }
}
