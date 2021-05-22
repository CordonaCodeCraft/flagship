package flagship.domain.renders.pda;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.*;
import flagship.domain.cases.dto.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class HeaderRender extends PdaElementsFactory {

  private static final String TITLE = "Proforma disbursement account";
  private static final String LOGO_PATH = "src/main/resources/images/logo.png";
  private static final String ISSUE_DATE =
      String.format(
          "Issue date: %s", LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM yyyy")));

  public Table renderHeader(final PdaCase source) throws IOException {

    Table table = getOuterTable();

    Cell left = table.getCell(0, 0);
    Cell right = table.getCell(0, 1);

    Text titleText = getText(TITLE, getBoldFont(), 18);
    Text dateText = getText(ISSUE_DATE, getBoldFont(), 12);
    Image logo = new Image(ImageDataFactory.create(LOGO_PATH));

    Paragraph titleParagraph = getParagraphTextAlignedRight(titleText);
    Paragraph dateParagraph = getParagraphTextAlignedRight(dateText);
    Paragraph logoParagraph = getParagraphImageAlignedRight(logo).setMarginTop(3);

    left.add(titleParagraph).add(dateParagraph);
    right.add(logoParagraph);

    log.info("Rendered document title, document date and company logo");

    return table;
  }
}
