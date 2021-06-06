package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.pda.model.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CompanyDetailsRender extends PdaElementsFactory {

  private static final String COMPANY_DETAILS = "Company details";
  private static final String ADDRESS = "Address";
  private static final String MOBILE = "Mobile";
  private static final String FAX = "Fax";
  private static final String EMAIL = "Email";

  public static Cell renderCompanyDetails(PdaCase source) throws IOException {
    final Text companyDetails = getText(COMPANY_DETAILS, getBoldFont(), 12).setUnderline();
    final Table companyDetailsTable = getCompanyDetailsTable(source);
    log.info("Rendered company details");
    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(companyDetails))
        .add(companyDetailsTable);
  }

  private static Table getCompanyDetailsTable(final PdaCase source) throws IOException {

    final Table companyDetailsTable = getInnerTable();

    final Text addressLabel = getText(ADDRESS, getBoldFont(), 10);
    final Text mobileLabel = getText(MOBILE, getBoldFont(), 10);
    final Text faxLabel = getText(FAX, getBoldFont(), 10);
    final Text emailLabel = getText(EMAIL, getBoldFont(), 10);

    final Text addressValue =
        getText("Varna, 35 Patron Street, entr. 2, floor 3, app.5", getRegularFont(), 10);
    final Text mobileValue = getText("(+359) 899921649", getRegularFont(), 10);
    final Text faxValue = getText("(+359) 42890789", getRegularFont(), 10);
    final Text emailFirstValue = getText("patronInfo@patronlogistics.eu", getRegularFont(), 10);
    final Text emailSecondValue = getText("patronContact@patronlogistics.eu", getRegularFont(), 10);

    companyDetailsTable
        .startNewRow()
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(addressLabel)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(addressValue)));

    companyDetailsTable
            .startNewRow()
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(mobileLabel)))
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(mobileValue)));

    companyDetailsTable
            .startNewRow()
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(faxLabel)))
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(faxValue)));

    companyDetailsTable
            .startNewRow()
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(emailLabel)))
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(emailFirstValue)));

    companyDetailsTable
            .startNewRow()
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedLeft(emailLabel)))
            .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(emailSecondValue)));

    return companyDetailsTable.setMarginLeft(15);
  }
}
