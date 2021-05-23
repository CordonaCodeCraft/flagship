package flagship.domain.renders.pda;

import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.cases.dto.PdaCase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

@Slf4j
public class FinancialsRender extends PdaElementsFactory {

  private static final String FINANCIALS = "Financials";
  private static final String COUNT = "No.";
  private static final String ITEM = "Item";
  private static final String PRICE = "Price";
  private static final String TOTAL_PAYABLE = "Total payable in euro";
  private static final String DISCOUNT = "Agency discount in euro";
  private static final String TOTAL = "Total in euro";

  public static Cell renderFinancials(PdaCase source) throws IOException {

    final Text financials = getText(FINANCIALS, getBoldFont(), 15);

    final Table financialsTable = getFinancialsTable(source);

    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(financials).setMarginTop(15))
        .add(financialsTable);
  }

  private static Table getFinancialsTable(final PdaCase source) throws IOException {

    final Text number = getText(COUNT, getBoldFont(), 12);
    final Text item = getText(ITEM, getBoldFont(), 12);
    final Text euro = getText(PRICE, getBoldFont(), 12);

    final Table financialsTable = getTable();

    financialsTable.addCell(getCellWithBottomBorder(30).add(getParagraphWithTextCentered(number)));
    financialsTable.addCell(getCellWithBottomBorder(230).add(getParagraphWithTextCentered(item)));
    financialsTable.addCell(
        getCellWithBottomBorder(120).add(getParagraphWithTextAlignedRight(euro)));

    populateFinancialsTable(source, financialsTable);

    final Text totalPayableText = getText(TOTAL_PAYABLE, getRegularFont(), 10);
    final Text discountText = getText(DISCOUNT, getRegularFont(), 10);
    final Text totalText = getText(TOTAL, getRegularFont(), 10);

    final Text totalPayableValue =
        getText(
            source.getProformaDisbursementAccount().getPayableTotal().toString(),
            getBoldFont(),
            10);

    final Text discountValue =
        getText(
            source.getProformaDisbursementAccount().getClientDiscount().toString(),
            getBoldFont(),
            10);

    final Text totalValue =
        getText(
            source.getProformaDisbursementAccount().getTotalAfterDiscount().toString(),
            getBoldFont(),
            10);

    financialsTable.addCell(
        getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(totalPayableText)));

    financialsTable.addCell(
        getCellWithNoBorder().add(getParagraphWithTextAlignedRight(totalPayableValue)));

    financialsTable.addCell(
        getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(discountText)));

    financialsTable.addCell(
        getCellWithNoBorder().add(getParagraphWithTextAlignedRight(discountValue)));

    financialsTable.addCell(
        getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(totalText)));

    financialsTable.addCell(
        getCellWithNoBorder().add(getParagraphWithTextAlignedRight(totalValue)));

    return financialsTable;
  }

  private static void populateFinancialsTable(final PdaCase source, final Table financials) {

    Arrays.stream(source.getProformaDisbursementAccount().getClass().getDeclaredFields())
        .filter(field -> isValidDueField(source, field))
        .forEach(field -> addRow(financials, source, field));
  }

  private static boolean isValidDueField(final PdaCase source, final Field field) {

    field.setAccessible(true);
    BigDecimal value = BigDecimal.ZERO;

    try {
      value = (BigDecimal) field.get(source.getProformaDisbursementAccount());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return field.getName().contains("Due") && value.doubleValue() > 0;
  }

  private static void addRow(final Table financials, final PdaCase source, final Field field) {

    field.setAccessible(true);

    final DecimalFormat df = new DecimalFormat("#,###.00");

    String dueString = null;

    try {
      dueString = df.format(field.get(source.getProformaDisbursementAccount()));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    Text count = null;
    Text dueType = null;
    Text due = null;

    try {
      count = getText(String.valueOf(financials.getNumberOfRows()), getRegularFont(), 10);
      dueType = getText(getDueType(field), getRegularFont(), 10);
      due = getText(dueString, getRegularFont(), 10);
    } catch (IOException e) {
      e.printStackTrace();
    }

    financials.addCell(getCellWithNoBorder(30).add(getParagraphWithTextCentered(count)));
    financials.addCell(getCellWithNoBorder(230).add(getParagraphWithTextAlignedLeft(dueType)));
    financials.addCell(getCellWithNoBorder(120).add(getParagraphWithTextAlignedRight(due)));
  }

  private static String getDueType(final Field field) {

    return Arrays.stream(PdaElementsFactory.DueType.values())
        .filter(due -> isMatch(due, field))
        .map(d -> d.type)
        .findFirst()
        .get();
  }

  private static boolean isMatch(final DueType due, final Field field) {
    final String dueSubstring = due.type.toLowerCase().substring(0, 3);
    final String fieldSubstring = field.getName().toLowerCase().substring(0, 3);
    return dueSubstring.contains(fieldSubstring);
  }

  private static Table getTable() {
    return new Table(3);
  }
}
