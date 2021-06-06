package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.pda.model.PdaCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.TextAlignment.CENTER;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType.*;

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

    final Table financialsTable = getTable();

    final Text numberLabel = getText(COUNT, getBoldFont(), 12);
    final Text itemLabel = getText(ITEM, getBoldFont(), 12);
    final Text euroLabel = getText(PRICE, getBoldFont(), 12);

    financialsTable
        .addCell(getCellWithBottomBorder(30).add(getParagraphWithTextCentered(numberLabel)))
        .addCell(getCellWithBottomBorder(230).add(getParagraphWithTextCentered(itemLabel)))
        .addCell(getCellWithBottomBorder(120).add(getParagraphWithTextAlignedRight(euroLabel)))
        .addCell(getCellWithBottomBorder());

    populateFinancialsTable(source, financialsTable);

    final Text totalPayableText = getText(TOTAL_PAYABLE, getRegularFont(), 10);
    final Text totalPayableValue =
        getText(
            source.getProformaDisbursementAccount().getPayableTotal().toString(),
            getBoldFont(),
            10);

    financialsTable
        .addCell(getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(totalPayableText)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(totalPayableValue)))
        .addCell(getCellWithNoBorder());

    final Text discountText = getText(DISCOUNT, getRegularFont(), 10);
    final Text discountValue =
        getText(
            source.getProformaDisbursementAccount().getClientDiscount().toString(),
            getBoldFont(),
            10);

    financialsTable
        .addCell(getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(discountText)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(discountValue)))
        .addCell(getCellWithNoBorder());

    final Text totalText = getText(TOTAL, getRegularFont(), 10);
    final Text totalValue =
        getText(
            source.getProformaDisbursementAccount().getTotalAfterDiscount().toString(),
            getBoldFont(),
            10);

    financialsTable
        .addCell(getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(totalText)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(totalValue)))
        .addCell(getCellWithNoBorder());

    return financialsTable;
  }

  private static void populateFinancialsTable(final PdaCase source, final Table financials) {
    Arrays.stream(source.getProformaDisbursementAccount().getClass().getDeclaredFields())
        .filter(field -> isValidDueField(source, field))
        .forEach(field -> addRow(financials, source, field));
  }

  private static boolean isValidDueField(final PdaCase source, final Field dueField) {
    dueField.setAccessible(true);
    BigDecimal value = BigDecimal.ZERO;
    try {
      value = (BigDecimal) dueField.get(source.getProformaDisbursementAccount());
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return dueField.getName().contains("Due") && value.doubleValue() > 0;
  }

  private static void addRow(final Table financials, final PdaCase source, final Field dueField) {

    final DecimalFormat df = new DecimalFormat("#,###.00");

    dueField.setAccessible(true);
    String dueString = null;

    try {
      dueString = df.format(dueField.get(source.getProformaDisbursementAccount()));
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    Text dueCount = getText(String.valueOf(financials.getNumberOfRows()), getRegularFont(), 10);
    Text dueType = getText(getDueType(dueField), getRegularFont(), 10);
    Text dueValue = getText(dueString, getRegularFont(), 10);

    StatusIconsAdvisor statusIconsAdvisor = new StatusIconsAdvisor(source, dueField);

    Paragraph statusIcons = statusIconsAdvisor.getStatusIcons();

    financials.addCell(getCellWithNoBorder(30).add(getParagraphWithTextCentered(dueCount)));
    financials.addCell(getCellWithNoBorder(230).add(getParagraphWithTextAlignedLeft(dueType)));
    financials.addCell(getCellWithNoBorder(120).add(getParagraphWithTextAlignedRight(dueValue)));
    financials.addCell(getCellWithNoBorder(30).add(statusIcons));
  }

  private static String getDueType(final Field dueField) {
    return Arrays.stream(PdaElementsFactory.DueType.values())
        .filter(due -> isMatch(due, dueField))
        .map(due -> due.type)
        .findFirst()
        .get();
  }

  private static boolean isMatch(final DueType due, final Field dueField) {
    final String dueSubstring = due.type.toLowerCase().substring(0, 3);
    final String fieldSubstring = dueField.getName().toLowerCase().substring(0, 3);
    return dueSubstring.contains(fieldSubstring);
  }

  private static Table getTable() {
    return new Table(4).setBorder(NO_BORDER);
  }

  @RequiredArgsConstructor
  private static class StatusIconsAdvisor {

    private final PdaCase source;
    private final Field dueField;

    private Paragraph getStatusIcons() {

      String due = dueField.getName();

      List<String> agencyDues =
          List.of(
              "basicAgencyDue",
              "carsDue",
              "clearanceDue",
              "communicationsDue",
              "bankExpensesDue",
              "agencyOvertimeDue");

      List<String> duesModifiedByWarnings =
          List.of("wharfDue", "pilotageDue", "tugDue", "mooringDue");

      if (agencyDues.contains(due)) {
        return getAgencyDueIcon();
      } else if (duesModifiedByWarnings.contains(due)) {
        return getDueDependentOnWarningsStatusIcons();
      } else {
        return getParagraphWithTextCentered(getChekMarkSymbol());
      }
    }

    private Paragraph getAgencyDueIcon() {
      if (source.getClientDiscountCoefficient().doubleValue() > 0) {
        return getParagraphWithTextCentered(getArrowDownSymbol());
      } else {
        return getParagraphWithTextCentered(getChekMarkSymbol());
      }
    }

    private Paragraph getDueDependentOnWarningsStatusIcons() {

      String due = dueField.getName();

      final Paragraph statusIcons = new Paragraph();

      if (source.getWarnings().isEmpty()) {
        statusIcons.add(getChekMarkSymbol());
      } else {
        if (due.equals("wharfDue")) {
          if (warningEventIs(ETD_NOT_PROVIDED)) {
            statusIcons.add(getQuestionMarkSymbol());
          } else if (warningEventIs(HOLIDAY)) {
            statusIcons.add(getArrowUpSymbol());
          } else {
            statusIcons.add(getChekMarkSymbol());
          }
        } else {
          if (warningEventIs(ETA_IS_HOLIDAY)) {
            statusIcons.add(getArrowUpSymbol());
          } else if (warningEventIs(ETA_NOT_PROVIDED)) {
            statusIcons.add(getQuestionMarkSymbol());
          } else {
            statusIcons.add(getChekMarkSymbol());
          }
          if (warningEventIs(ETD_IS_HOLIDAY)) {
            statusIcons.add(getArrowUpSymbol());
          } else if (warningEventIs(ETD_NOT_PROVIDED)) {
            statusIcons.add(getQuestionMarkSymbol());
          } else {
            statusIcons.add(getChekMarkSymbol());
          }
        }
      }

      return statusIcons.setTextAlignment(CENTER);
    }

    private boolean warningEventIs(final WarningType warningEvent) {
      return source.getWarnings().stream()
          .anyMatch(warning -> warning.getWarningType() == warningEvent);
    }
  }
}
