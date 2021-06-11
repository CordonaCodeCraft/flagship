package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.caze.model.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class PaymentDetailsRender extends PdaElementsFactory {

  private static final String PAYMENT_DETAILS_TITLE = "Payment details";
  private static final String BENEFICIARY_LABEL = "Beneficiary";
  private static final String BANK_NAME_LABEL = "Bank name";
  private static final String SWIFT_LABEL = "SWIFT (BIC) code";
  private static final String IBAN_LABEL = "IBAN (Euro)";

  public static Cell renderPaymentDetails(final PdaCase source) throws IOException {
    final Text companyDetailsTitle = getText(PAYMENT_DETAILS_TITLE, getBoldFont(), 12);
    final Table paymentDetail = getPaymentDetailsTable(source);
    log.info("Rendered payment details");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(companyDetailsTitle))
        .add(paymentDetail);
  }

  private static Table getPaymentDetailsTable(final PdaCase source) throws IOException {

    final Table paymentDetailsTable = getInnerTable();

    final Text beneficiaryLabel = getText(BENEFICIARY_LABEL, getBoldFont(), 10);
    final Text bankNameLabel = getText(BANK_NAME_LABEL, getBoldFont(), 10);
    final Text swiftLabel = getText(SWIFT_LABEL, getBoldFont(), 10);
    final Text ibanLabel = getText(IBAN_LABEL, getBoldFont(), 10);

    final Text beneficiaryValue = getText("Patron Logistics Ltd", getRegularFont(), 10);
    final Text bankNameValue = getText("Patron Bank", getRegularFont(), 10);
    final Text swiftValue = getText("PATBGSF", getRegularFont(), 10);
    final Text ibanValue = getText("BG99PAT999888777", getRegularFont(), 10);

    paymentDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(beneficiaryLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(beneficiaryValue)));

    paymentDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(bankNameLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(bankNameValue)));

    paymentDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(swiftLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(swiftValue)));

    paymentDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(ibanLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(ibanValue)));

    return paymentDetailsTable.setMarginLeft(15);
  }
}
