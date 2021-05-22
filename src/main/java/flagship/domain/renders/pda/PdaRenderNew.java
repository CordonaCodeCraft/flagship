package flagship.domain.renders.pda;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import flagship.domain.cases.dto.PdaCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PdaRenderNew extends PdaElementsFactory {

  private final PdaCase source;
  private final HeaderRender headerRender = new HeaderRender();
  private final DetailsRender detailsRender = new DetailsRender();
  private final FinancialsRender financialsRender = new FinancialsRender();

  public void renderPdaDocument() throws IOException {

    final String filePath = "D:/Gdrive/Inbox/" + getFileName(source);

    Document pda = getNewDocument(filePath);

    Table header = headerRender.renderHeader(source);
    Table details = detailsRender.renderDetails(source);
    Table financialsAndNotes = getOuterTable();
    Cell financials = financialsRender.renderFinancials(source);
    financialsAndNotes.addCell(financials);


    pda.add(header.setMarginBottom(10));
    pda.add(details);

    pda.close();
  }

  private String getFileName(final PdaCase source) {
    return String.format("%s_%s.pdf", source.getShip().getName(), LocalDate.now().toString());
  }
}
