package flagship.domain.pda.render.elements;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import flagship.domain.caze.model.PdaCase;
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

  private static final String FINANCIALS_TITLE = "Financials";
  private static final String COUNT_LABEL = "No.";
  private static final String ITEM_LABEL = "Item";
  private static final String PRICE_LABEL = "Price";
  private static final String TOTAL_PAYABLE_LABEL = "Total payable in euro";
  private static final String DISCOUNT_LABEL = "Agency discount in euro";
  private static final String TOTAL_LABEL = "Total in euro";

  public static Cell renderFinancials(final PdaCase source) throws IOException {

    final Text financialsTitle = getText(FINANCIALS_TITLE, getBoldFont(), 12);
    final Table financialsTable = getFinancialsTable(source);

    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(financialsTitle))
        .add(financialsTable);
  }

  private static Table getFinancialsTable(final PdaCase source) throws IOException {

    final Table financialsTable = getTable();

    final Text numberLabel = getText(COUNT_LABEL, getBoldFont(), 11);
    final Text itemLabel = getText(ITEM_LABEL, getBoldFont(), 11);
    final Text euroLabel = getText(PRICE_LABEL, getBoldFont(), 11);

    financialsTable
        .startNewRow()
        .addCell(getCellWithNoBorder(30).add(getParagraphWithTextCentered(numberLabel)))
        .addCell(getCellWithNoBorder(230).add(getParagraphWithTextCentered(itemLabel)))
        .addCell(getCellWithNoBorder(120).add(getParagraphWithTextAlignedRight(euroLabel)))
        .addCell(getCellWithNoBorder());

    populateFinancialsTable(source, financialsTable);

    final Text totalPayableText = getText(TOTAL_PAYABLE_LABEL, getRegularFont(), 10);
    final Text totalPayableValue =
        getText(
            source.getProformaDisbursementAccount().getPayableTotal().toString(),
            getBoldFont(),
            10);

    financialsTable
        .addCell(getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(totalPayableText)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(totalPayableValue)))
        .addCell(getCellWithNoBorder());

    final Text discountText = getText(DISCOUNT_LABEL, getRegularFont(), 10);
    final Text discountValue =
        getText(
            source.getProformaDisbursementAccount().getClientDiscount().toString(),
            getBoldFont(),
            10);

    financialsTable
        .addCell(getCellWithNoBorder(1, 2).add(getParagraphWithTextAlignedRight(discountText)))
        .addCell(getCellWithNoBorder().add(getParagraphWithTextAlignedRight(discountValue)))
        .addCell(getCellWithNoBorder());

    final Text totalText = getText(TOTAL_LABEL, getRegularFont(), 10);
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
    } catch (final IllegalAccessException e) {
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
    } catch (final IllegalAccessException e) {
      e.printStackTrace();
    }

    final Text dueCount =
        getText(String.valueOf(financials.getNumberOfRows()), getRegularFont(), 10);
    final Text dueType = getText(getDueType(dueField), getRegularFont(), 10);
    final Text dueValue = getText(dueString, getRegularFont(), 10);

    final StatusIconsGenerator statusIconsGenerator = new StatusIconsGenerator(source, dueField);

    final Paragraph statusIcons = statusIconsGenerator.generateStatusIcons();

    final Color rowColor =
        financials.getNumberOfRows() % 2 == 0 ? getColorYellowMid() : getColorYellowLight();

    financials
        .startNewRow()
        .addCell(
            getCellWithNoBorder(30)
                .add(getParagraphWithTextCentered(dueCount))
                .setBackgroundColor(rowColor))
        .addCell(
            getCellWithNoBorder(230)
                .add(getParagraphWithTextAlignedLeft(dueType))
                .setBackgroundColor(rowColor))
        .addCell(
            getCellWithNoBorder(120)
                .add(getParagraphWithTextAlignedRight(dueValue))
                .setBackgroundColor(rowColor))
        .addCell(getCellWithNoBorder(30).add(statusIcons).setBackgroundColor(rowColor));
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
    return new Table(4).setBorder(NO_BORDER).setBackgroundColor(getColorYellowDark());
  }

  @RequiredArgsConstructor
  private static class StatusIconsGenerator {

    private final PdaCase source;
    private final Field dueField;

    private Paragraph generateStatusIcons() {

      final String due = dueField.getName();

      final List<String> agencyDues =
          List.of(
              "basicAgencyDue",
              "carsDue",
              "clearanceDue",
              "communicationsDue",
              "bankExpensesDue",
              "agencyOvertimeDue");

      final List<String> duesModifiedByWarnings =
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

      final String due = dueField.getName();

      final Paragraph statusIcons = new Paragraph();

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

      return statusIcons.setTextAlignment(CENTER);
    }

    private boolean warningEventIs(final WarningType warningEvent) {
      return source.getWarnings().stream()
          .anyMatch(warning -> warning.getWarningType() == warningEvent);
    }
  }
}
