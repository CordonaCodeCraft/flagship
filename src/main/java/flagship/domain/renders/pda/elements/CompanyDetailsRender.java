package flagship.domain.renders.pda.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.cases.dto.PdaCase;
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

    final Cell left = companyDetailsTable.getCell(0, 0);
    final Cell right = companyDetailsTable.getCell(0, 1);

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

    left.add(getParagraphWithTextAlignedLeft(addressLabel));
    left.add(getParagraphWithTextAlignedLeft(mobileLabel));
    left.add(getParagraphWithTextAlignedLeft(faxLabel));
    left.add(getParagraphWithTextAlignedLeft(emailLabel));
    left.add(getParagraphWithTextAlignedLeft(emailLabel));

    right.add(getParagraphWithTextAlignedRight(addressValue));
    right.add(getParagraphWithTextAlignedRight(mobileValue));
    right.add(getParagraphWithTextAlignedRight(faxValue));
    right.add(getParagraphWithTextAlignedRight(emailFirstValue));
    right.add(getParagraphWithTextAlignedRight(emailSecondValue));

    return companyDetailsTable.setMarginLeft(15);
  }
}
