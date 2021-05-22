package flagship.domain.renders.pda;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.cases.dto.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class DetailsRender extends PdaElementsFactory {

  private static final String SHIP_PARTICULARS = "Ship particulars";
  private static final String SHIP_NAME = "Name";
  private static final String SHIP_TYPE = "Type";
  private static final String SHIP_GT = "Gross tonnage";
  private static final String SHIP_LOA = "Length overall";
  private static final String CALL_DETAILS = "Call details";
  private static final String CALL_COUNT = "No. of call";
  private static final String CALL_PURPOSE = "Purpose of call";
  private static final String ALONGSIDE_DAYS = "Vessel alongside for";
  private static final String CARGO_MANIFEST = "Cargo manifest";
  private static final String COMPANY_DETAILS = "Company details";
  private static final String ADDRESS = "Address";
  private static final String MOBILE = "Mobile";
  private static final String FAX = "Fax";
  private static final String EMAIL = "Email";
  private static final String PAYMENT_DETAILS = "Payment details";
  private static final String BENEFICIARY = "Beneficiary";
  private static final String BANK_NAME = "Bank name";
  private static final String SWIFT = "SWIFT (BIC) code";
  private static final String IBAN = "IBAN (Euro)";

  public Table renderDetails(final PdaCase source) throws IOException {

    Table outerTable = getOuterTable();

    Cell left = outerTable.getCell(0, 0);
    Cell right = outerTable.getCell(0, 1);

    left.add(renderShipParticulars(source, left)).add(renderCallDetailsTable(source, left));
    right.add(renderCompanyDetails(source, right)).add(renderPaymentDetails(source, right));

    renderCargoDetails(source, left);

    return outerTable;
  }

  private void renderCargoDetails(final PdaCase source, Cell container) throws IOException {

    String cargoList = String.join(", ", source.getCargoManifest());
    Text cargoListText = getText(cargoList, getRegularFont(), 10);

    Text cargoManifest = getText(CARGO_MANIFEST, getBoldFont(), 12).setUnderline();

    container
        .add(getParagraphTextAlignedRight(cargoManifest))
        .add(getParagraphTextAlignedRight(cargoListText));
  }

  private Table renderShipParticulars(final PdaCase source, final Cell container)
      throws IOException {

    Text shipParticulars = getText(SHIP_PARTICULARS, getBoldFont(), 12).setUnderline();

    container.add(getParagraphTextAlignedRight(shipParticulars));

    Table shipParticularsTable = getInnerTable();

    Cell left = shipParticularsTable.getCell(0, 0);
    Cell right = shipParticularsTable.getCell(0, 1);

    Text shipNameLabel = getText(SHIP_NAME, getBoldFont(), 10);
    Text shipTypeLabel = getText(SHIP_TYPE, getBoldFont(), 10);
    Text shipGtLabel = getText(SHIP_GT, getBoldFont(), 10);
    Text shipLoaLabel = getText(SHIP_LOA, getBoldFont(), 10);

    String shipName = source.getShip().getName();
    String shipType = source.getShip().getType().type;
    String shipGt = source.getShip().getGrossTonnage().toString();
    String shipLoa = String.valueOf(source.getShip().getLengthOverall().intValue());

    Text shipNameValue = getText(shipName, getRegularFont(), 10);
    Text shipTypeValue = getText(shipType, getRegularFont(), 10);
    Text shipGtValue = getText(shipGt + " metric tons", getRegularFont(), 10);
    Text shipLoaValue = getText(shipLoa +" meters", getRegularFont(), 10);

    left.add(getCell().add(getParagraphTextAlignedLeft(shipNameLabel)));
    left.add(getCell().add(getParagraphTextAlignedLeft(shipTypeLabel)));
    left.add(getCell().add(getParagraphTextAlignedLeft(shipGtLabel)));
    left.add(getCell().add(getParagraphTextAlignedLeft(shipLoaLabel)));

    right.add(getCell().add(getParagraphTextAlignedRight(shipNameValue)));
    right.add(getCell().add(getParagraphTextAlignedRight(shipTypeValue)));
    right.add(getCell().add(getParagraphTextAlignedRight(shipGtValue)));
    right.add(getCell().add(getParagraphTextAlignedRight(shipLoaValue)));

    log.info("Rendered Ship particulars");

    return shipParticularsTable;
  }

  private Table renderCallDetailsTable(final PdaCase source, final Cell container)
      throws IOException {

    Text callDetails = getText(CALL_DETAILS, getBoldFont(), 12).setUnderline();

    container.add(getParagraphTextAlignedRight(callDetails));

    Table callDetailsTable = getInnerTable();

    Cell left = callDetailsTable.getCell(0, 0);
    Cell right = callDetailsTable.getCell(0, 1);

    Text callCountLabel = getText(CALL_COUNT, getBoldFont(), 10);
    Text callPurposeLabel = getText(CALL_PURPOSE, getBoldFont(), 10);
    Text alongsideDaysLabel = getText(ALONGSIDE_DAYS, getBoldFont(), 10);

    String callCount = String.valueOf(source.getCallCount());
    String callPurpose = source.getCallPurpose().type;
    String alongsideDays = String.valueOf(source.getAlongsideDaysExpected());

    Text callCountText = getText(callCount, getRegularFont(), 10);
    Text callPurposeText = getText(callPurpose, getRegularFont(), 10);
    Text alongsideDaysText = getText(alongsideDays + " days", getRegularFont(), 10);

    left.add(getCell().add(getParagraphTextAlignedLeft(callCountLabel)));
    left.add(getCell().add(getParagraphTextAlignedLeft(callPurposeLabel)));
    left.add(getCell().add(getParagraphTextAlignedLeft(alongsideDaysLabel)));

    right.add(getCell().add(getParagraphTextAlignedRight(callCountText)));
    right.add(getCell().add(getParagraphTextAlignedRight(callPurposeText)));
    right.add(getCell().add(getParagraphTextAlignedRight(alongsideDaysText)));

    log.info("Rendered Call details");

    return callDetailsTable;
  }

  private Table renderCompanyDetails(final PdaCase source, final Cell container)
      throws IOException {

    Text companyDetails = getText(COMPANY_DETAILS, getBoldFont(), 12).setUnderline();
    container.add(getParagraphTextAlignedRight(companyDetails));

    Table companyDetailsTable = getInnerTable();

    Cell left = companyDetailsTable.getCell(0, 0);
    Cell right = companyDetailsTable.getCell(0, 1);

    Text addressLabel = getText(ADDRESS, getBoldFont(), 10);
    Text mobileLabel = getText(MOBILE, getBoldFont(), 10);
    Text faxLabel = getText(FAX, getBoldFont(), 10);
    Text emailLabel = getText(EMAIL, getBoldFont(), 10);

    Text addressValue =
        getText("Varna, 35 Patron Street, entr. 2, floor 3, app.5", getRegularFont(), 10);
    Text mobileValue = getText("(+359) 899921649", getRegularFont(), 10);
    Text faxValue = getText("(+359) 42890789", getRegularFont(), 10);
    Text emailFirst = getText("patronInfo@patronlogistics.eu", getRegularFont(), 10);
    Text emailSecond = getText("patronContact@patronlogistics.eu", getRegularFont(), 10);

    left.add(getParagraphTextAlignedLeft(addressLabel));
    left.add(getParagraphTextAlignedLeft(mobileLabel));
    left.add(getParagraphTextAlignedLeft(faxLabel));
    left.add(getParagraphTextAlignedLeft(emailLabel));
    left.add(getParagraphTextAlignedLeft(emailLabel));

    right.add(getParagraphTextAlignedRight(addressValue));
    right.add(getParagraphTextAlignedRight(mobileValue));
    right.add(getParagraphTextAlignedRight(faxValue));
    right.add(getParagraphTextAlignedRight(emailFirst));
    right.add(getParagraphTextAlignedRight(emailSecond));

    return companyDetailsTable.setMarginLeft(15);
  }

  private Table renderPaymentDetails(final PdaCase source, final Cell container)
          throws IOException {

    Text companyDetails = getText(PAYMENT_DETAILS, getBoldFont(), 12).setUnderline();
    container.add(getParagraphTextAlignedRight(companyDetails));

    Table paymentDetailsTable = getInnerTable();

    Cell left = paymentDetailsTable.getCell(0, 0);
    Cell right = paymentDetailsTable.getCell(0, 1);

    Text beneficiaryLabel = getText(BENEFICIARY, getBoldFont(), 10);
    Text bankNameLabel = getText(BANK_NAME, getBoldFont(), 10);
    Text swiftLabel = getText(SWIFT, getBoldFont(), 10);
    Text ibanLabel = getText(IBAN, getBoldFont(), 10);

    Text beneficiaryValue = getText("Patron Logistics Ltd", getRegularFont(),10);
    Text bankNameValue = getText("Patron Bank", getRegularFont(),10);
    Text swiftValue = getText("PATBGSF", getRegularFont(),10);
    Text ibanValue = getText("BG99PAT999888777", getRegularFont(),10);

    left.add(getParagraphTextAlignedLeft(beneficiaryLabel));
    left.add(getParagraphTextAlignedLeft(bankNameLabel));
    left.add(getParagraphTextAlignedLeft(swiftLabel));
    left.add(getParagraphTextAlignedLeft(ibanLabel));

    right.add(getParagraphTextAlignedRight(beneficiaryValue));
    right.add(getParagraphTextAlignedRight(bankNameValue));
    right.add(getParagraphTextAlignedRight(swiftValue));
    right.add(getParagraphTextAlignedRight(ibanValue));

    return paymentDetailsTable.setMarginLeft(15);
  }
}
