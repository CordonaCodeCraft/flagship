package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.pda.model.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CallDetailsRender extends PdaElementsFactory {

  private static final String CALL_DETAILS = "Call details";
  private static final String CALL_COUNT = "No. of call";
  private static final String CALL_PURPOSE = "Purpose of call";
  private static final String ALONGSIDE_DAYS = "Vessel alongside for";
  private static final String CARGO = "Cargo";
  private static final String PORT_OF_CALL = "Port of call";

  public static Cell renderCallDetails(final PdaCase source) throws IOException {
    final Text callDetails = getText(CALL_DETAILS, getBoldFont(), 12).setUnderline();
    final Text cargo = getText(CARGO, getBoldFont(), 12).setUnderline();
    final String cargoList = String.join(", ", source.getCargoManifest());
    final Text cargoListText = getText(cargoList, getRegularFont(), 10);
    final Table callDetailsTable = getCallDetailsTable(source);

    log.info("Rendered call details");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(callDetails))
        .add(callDetailsTable)
        .add(getParagraphWithTextAlignedRight(cargo))
        .add(getParagraphWithTextAlignedRight(cargoListText));
  }

  private static Table getCallDetailsTable(final PdaCase source) throws IOException {

    final Table callDetailsTable = getInnerTable();

    final Cell left = callDetailsTable.getCell(0, 0);
    final Cell right = callDetailsTable.getCell(0, 1);

    final Text callCountLabel = getText(CALL_COUNT, getBoldFont(), 10);
    final Text callPurposeLabel = getText(CALL_PURPOSE, getBoldFont(), 10);
    final Text alongsideDaysLabel = getText(ALONGSIDE_DAYS, getBoldFont(), 10);
    final Text portOfCallLabel = getText(PORT_OF_CALL, getBoldFont(), 10);

    final String callCountValue = String.valueOf(source.getCallCount());
    final String callPurposeValue = source.getCallPurpose().type;
    final String alongsideDaysValue = String.valueOf(source.getAlongsideDaysExpected());
    final String portOfValue = source.getPort().getName();

    final Text callCountText = getText(callCountValue, getRegularFont(), 10);
    final Text callPurposeText = getText(callPurposeValue, getRegularFont(), 10);
    final Text alongsideDaysText = getText(alongsideDaysValue + " days", getRegularFont(), 10);
    final Text portOfCallText = getText(portOfValue, getRegularFont(), 10);

    left.add(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(callCountLabel)));
    left.add(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(callPurposeLabel)));
    left.add(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(alongsideDaysLabel)));
    left.add(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(portOfCallLabel)));

    right.add(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(callCountText)));
    right.add(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(callPurposeText)));
    right.add(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(alongsideDaysText)));
    right.add(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(portOfCallText)));

    return callDetailsTable;
  }
}
