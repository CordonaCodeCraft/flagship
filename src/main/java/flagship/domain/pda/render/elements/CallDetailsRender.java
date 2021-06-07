package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.pda.model.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CallDetailsRender extends PdaElementsFactory {

  private static final String CALL_DETAILS_TITLE = "Call details";
  private static final String CALL_COUNT_LABEL = "No. of call";
  private static final String CALL_PURPOSE_LABEL = "Purpose of call";
  private static final String ALONGSIDE_DAYS_LABEL = "Vessel alongside for";
  private static final String CARGO_TITLE = "Cargo";
  private static final String PORT_OF_CALL_LABEL = "Port of call";

  public static Cell renderCallDetails(final PdaCase source) throws IOException {

    final Text callDetailsTitle = getText(CALL_DETAILS_TITLE, getBoldFont(), 12);
    final Text cargoTitle = getText(CARGO_TITLE, getBoldFont(), 12);
    final String cargoList = String.join(", ", source.getCargoManifest());
    final Text cargoListText = getText(cargoList, getRegularFont(), 10);
    final Table callDetailsTable = getCallDetailsTable(source);

    log.info("Rendered call details");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(callDetailsTitle))
        .add(callDetailsTable)
        .add(getParagraphWithTextAlignedRight(cargoTitle))
        .add(getParagraphWithTextAlignedRight(cargoListText));
  }

  private static Table getCallDetailsTable(final PdaCase source) throws IOException {

    final Table callDetailsTable = getInnerTable();

    final Text callCountLabel = getText(CALL_COUNT_LABEL, getBoldFont(), 10);
    final Text callPurposeLabel = getText(CALL_PURPOSE_LABEL, getBoldFont(), 10);
    final Text alongsideDaysLabel = getText(ALONGSIDE_DAYS_LABEL, getBoldFont(), 10);
    final Text portOfCallLabel = getText(PORT_OF_CALL_LABEL, getBoldFont(), 10);

    final String callCountValue = String.valueOf(source.getCallCount());
    final String callPurposeValue = source.getCallPurpose().type;
    final String alongsideDaysValue = String.valueOf(source.getAlongsideDaysExpected());
    final String portOfValue = source.getPort().getName();

    final Text callCountText = getText(callCountValue, getRegularFont(), 10);
    final Text callPurposeText = getText(callPurposeValue, getRegularFont(), 10);
    final Text alongsideDaysText = getText(alongsideDaysValue + " days", getRegularFont(), 10);
    final Text portOfCallText = getText(portOfValue, getRegularFont(), 10);

    callDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(callCountLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(callCountText)));

    callDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(callPurposeLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(callPurposeText)));

    callDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(alongsideDaysLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(alongsideDaysText)));

    callDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(portOfCallLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(portOfCallText)));

    return callDetailsTable;
  }
}
