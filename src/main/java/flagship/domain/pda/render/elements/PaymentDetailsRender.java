package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.pda.model.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class PaymentDetailsRender extends PdaElementsFactory {

  private static final String PAYMENT_DETAILS = "Payment details";
  private static final String BENEFICIARY = "Beneficiary";
  private static final String BANK_NAME = "Bank name";
  private static final String SWIFT = "SWIFT (BIC) code";
  private static final String IBAN = "IBAN (Euro)";

  public static Cell renderPaymentDetails(PdaCase source) throws IOException {
    final Text companyDetails = getText(PAYMENT_DETAILS, getBoldFont(), 12).setUnderline();
    final Table paymentDetail = getPaymentDetailsTable(source);
    log.info("Rendered payment details");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(companyDetails))
        .add(paymentDetail);
  }

  private static Table getPaymentDetailsTable(final PdaCase source) throws IOException {

    final Table paymentDetailsTable = getInnerTable();

    final Cell left = paymentDetailsTable.getCell(0, 0);
    final Cell right = paymentDetailsTable.getCell(0, 1);

    final Text beneficiaryLabel = getText(BENEFICIARY, getBoldFont(), 10);
    final Text bankNameLabel = getText(BANK_NAME, getBoldFont(), 10);
    final Text swiftLabel = getText(SWIFT, getBoldFont(), 10);
    final Text ibanLabel = getText(IBAN, getBoldFont(), 10);

    final Text beneficiaryValue = getText("Patron Logistics Ltd", getRegularFont(), 10);
    final Text bankNameValue = getText("Patron Bank", getRegularFont(), 10);
    final Text swiftValue = getText("PATBGSF", getRegularFont(), 10);
    final Text ibanValue = getText("BG99PAT999888777", getRegularFont(), 10);

    left.add(getParagraphWithTextAlignedLeft(beneficiaryLabel));
    left.add(getParagraphWithTextAlignedLeft(bankNameLabel));
    left.add(getParagraphWithTextAlignedLeft(swiftLabel));
    left.add(getParagraphWithTextAlignedLeft(ibanLabel));

    right.add(getParagraphWithTextAlignedRight(beneficiaryValue));
    right.add(getParagraphWithTextAlignedRight(bankNameValue));
    right.add(getParagraphWithTextAlignedRight(swiftValue));
    right.add(getParagraphWithTextAlignedRight(ibanValue));

    return paymentDetailsTable.setMarginLeft(15);
  }
}
