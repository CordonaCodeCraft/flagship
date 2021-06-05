package flagship.domain.renders.pda.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Text;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TitleRender extends PdaElementsFactory {

  private static final String TITLE = "Proforma Disbursement Account";
  private static final String ISSUE_DATE =
      String.format(
          "Issue date: %s", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM yyyy")));

  public static Cell renderTitle() throws IOException {
    final Text titleText = getText(TITLE, getBoldFont(), 17);
    final Text dateText = getText(ISSUE_DATE, getBoldFont(), 12);
    log.info("Rendered title");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(titleText))
        .add(getParagraphWithTextAlignedRight(dateText));
  }
}
