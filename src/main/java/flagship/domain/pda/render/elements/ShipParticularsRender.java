package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.pda.model.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ShipParticularsRender extends PdaElementsFactory {

  private static final String SHIP_PARTICULARS_TITLE = "Ship particulars";
  private static final String SHIP_NAME_LABEL = "Name";
  private static final String SHIP_TYPE_LABEL = "Type";
  private static final String SHIP_GT_LABEL = "Gross tonnage";
  private static final String SHIP_LOA_LABEL = "Length overall";

  public static Cell renderShipParticulars(PdaCase source) throws IOException {
    final Text shipParticularsTitle = getText(SHIP_PARTICULARS_TITLE, getBoldFont(), 12);
    final Table shipParticularsTable = getShipParticularsTable(source);
    log.info("Rendered ship particulars");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(shipParticularsTitle))
        .add(shipParticularsTable);
  }

  private static Table getShipParticularsTable(final PdaCase source) throws IOException {

    final Table shipParticularsTable = getInnerTable();

    final Text shipNameLabel = getText(SHIP_NAME_LABEL, getBoldFont(), 10);
    final Text shipTypeLabel = getText(SHIP_TYPE_LABEL, getBoldFont(), 10);
    final Text shipGtLabel = getText(SHIP_GT_LABEL, getBoldFont(), 10);
    final Text shipLoaLabel = getText(SHIP_LOA_LABEL, getBoldFont(), 10);

    final String shipName = source.getShip().getName();
    final String shipType = source.getShip().getType().type;
    final String shipGt = source.getShip().getGrossTonnage().toString();
    final String shipLoa = String.valueOf(source.getShip().getLengthOverall().intValue());

    final Text shipNameValue = getText(shipName, getRegularFont(), 10);
    final Text shipTypeValue = getText(shipType, getRegularFont(), 10);
    final Text shipGtValue = getText(shipGt + " gt", getRegularFont(), 10);
    final Text shipLoaValue = getText(shipLoa + " meters", getRegularFont(), 10);

    shipParticularsTable
        .startNewRow()
        .addCell((getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(shipNameLabel))))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(shipNameValue)));

    shipParticularsTable
        .startNewRow()
        .addCell((getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(shipTypeLabel))))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(shipTypeValue)));

    shipParticularsTable
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(shipGtLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(shipGtValue)));

    shipParticularsTable
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(shipLoaLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(shipLoaValue)));

    return shipParticularsTable;
  }
}
