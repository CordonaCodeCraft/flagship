package flagship.domain.pda.render.elements;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import flagship.domain.caze.model.PdaCase;
import flagship.domain.warning.entity.Warning;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static flagship.domain.pda.render.elements.PdaElementsFactory.DueType.*;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType;
import static flagship.domain.warning.generator.WarningsGenerator.WarningType.*;

@Slf4j
public class InformationRender extends PdaElementsFactory {

  private static final String INFORMATION_TITLE = "Information";
  private static final String GARBAGE_DISPOSAL_TITLE = "Garbage disposal";
  private static final String LEGEND_TITLE = "Legend";
  private static final String ETA_WARNINGS_TITLE = "ETA warnings";
  private static final String ETD_WARNINGS_TITLE = "ETD warnings";

  public static Cell renderInformation(final PdaCase source) {

    final Text informationTitle = getText(INFORMATION_TITLE, getBoldFont(), 12);
    final Text etaWarningsTitle = getText(ETA_WARNINGS_TITLE, getBoldFont(), 11);
    final Text etdWarningsTitle = getText(ETD_WARNINGS_TITLE, getBoldFont(), 11);
    final Text garbageDisposalTitle = getText(GARBAGE_DISPOSAL_TITLE, getBoldFont(), 11);
    final Text legendTitle = getText(LEGEND_TITLE, getBoldFont(), 12);

    return getCellWithNoBorder()
        .add(getParagraphWithTextAlignedRight(informationTitle))
        .add(getParagraphWithTextAlignedRight(etaWarningsTitle))
        .add(getEtaWarnings(source))
        .add(getParagraphWithTextAlignedRight(etdWarningsTitle))
        .add(getEtdWarnings(source))
        .add(getParagraphWithTextAlignedRight(garbageDisposalTitle))
        .add(getGarbageReport(source))
        //        .add(getParagraphWithTextAlignedRight(legendTitle))
        //        .add(getLegendTable())
        .setPaddingLeft(15);
  }

  private static Paragraph getEtaWarnings(final PdaCase source) {

    Paragraph etaWarnings = new Paragraph();

    if (!containsWarning(source, ETA_NOT_PROVIDED)) {
      etaWarnings =
          getParagraphWithTextAlignedRight(getText("No warnings", getRegularFont(), 10))
              .setBackgroundColor(getColorGreen());
    } else if (containsWarning(source, ETA_NOT_PROVIDED)) {
      etaWarnings =
          getParagraphWithTextJustified(
                  getText(
                      "ETA not provided. Cannot evaluate increase for pilotage, tug and mooring/unmooring services",
                      getRegularFont(),
                      10))
              .setBackgroundColor(getColorRed());
    } else if (containsWarning(source, ETA_IS_HOLIDAY)) {

      final BigDecimal pilotageDueFactor = getIncreaseFactor(source, ETA_IS_HOLIDAY, PILOTAGE_DUE);
      final BigDecimal tugDueFactor = getIncreaseFactor(source, ETA_IS_HOLIDAY, TUG_DUE);
      final BigDecimal mooringDueFactor = getIncreaseFactor(source, ETA_IS_HOLIDAY, MOORING_DUE);
      final StringBuilder warningMessage = new StringBuilder();

      warningMessage.append("Expect increase for the bellow services:");
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Pilotage by factor of %s", pilotageDueFactor));
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Tug services by factor of %s", tugDueFactor));
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Mooring & Unmooring by factor of %s", mooringDueFactor));

      etaWarnings =
          getParagraphWithTextAlignedRight(getText(warningMessage.toString(), getRegularFont(), 10))
              .setBackgroundColor(getColorRed());
    }

    return etaWarnings.setPaddings(5, 5, 5, 5);
  }

  private static Paragraph getEtdWarnings(final PdaCase source) {

    Paragraph etdWarnings = new Paragraph();

    if (!containsWarning(source, ETD_NOT_PROVIDED)) {
      etdWarnings =
          getParagraphWithTextAlignedRight(getText("No warnings", getRegularFont(), 10))
              .setBackgroundColor(getColorGreen());
    } else if (containsWarning(source, ETD_NOT_PROVIDED)) {
      etdWarnings =
          getParagraphWithTextJustified(
                  getText(
                      "ETD not provided. Cannot evaluate increase for wharf due, pilotage, tug and mooring/unmooring services",
                      getRegularFont(),
                      10))
              .setBackgroundColor(getColorRed());
    } else if (containsWarning(source, ETD_IS_HOLIDAY)) {

      final Long wharfDueFactor = getWharfDueIncreaseFactor(source, HOLIDAY, WHARF_DUE);
      final BigDecimal pilotageDueFactor = getIncreaseFactor(source, ETD_IS_HOLIDAY, PILOTAGE_DUE);
      final BigDecimal tugDueFactor = getIncreaseFactor(source, ETD_IS_HOLIDAY, TUG_DUE);
      final BigDecimal mooringDueFactor = getIncreaseFactor(source, ETD_IS_HOLIDAY, MOORING_DUE);

      final StringBuilder warningMessage = new StringBuilder();
      warningMessage.append("Expect increase for the bellow services:");
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Wharf due for %s more days", wharfDueFactor));
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Pilotage by factor of %s", pilotageDueFactor));
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Tug services by factor of %s", tugDueFactor));
      warningMessage.append(System.lineSeparator());
      warningMessage.append(String.format("Mooring & Unmooring by factor of %s", mooringDueFactor));

      etdWarnings =
          getParagraphWithTextAlignedRight(getText(warningMessage.toString(), getRegularFont(), 10))
              .setBackgroundColor(getColorRed());
    }
    return etdWarnings.setPaddings(5, 5, 5, 5);
  }

  private static BigDecimal getIncreaseFactor(
      final PdaCase source, final WarningType warningType, final DueType dueType) {
    return source.getWarnings().stream()
        .filter(warning -> isMatch(warningType, dueType, warning))
        .map(Warning::getWarningFactor)
        .findFirst()
        .get();
  }

  private static Long getWharfDueIncreaseFactor(
      final PdaCase source, final WarningType warningType, final DueType dueType) {
    return source.getWarnings().stream()
        .filter(warning -> isMatch(warningType, dueType, warning))
        .count();
  }

  private static boolean containsWarning(final PdaCase source, final WarningType warningType) {
    return source.getWarnings().stream()
        .anyMatch(warning -> warning.getWarningType() == warningType);
  }

  private static boolean isMatch(
      final WarningType warningType, final DueType dueType, final Warning warning) {
    return warning.getWarningType() == warningType && warning.getDueType() == dueType;
  }

  private static Paragraph getGarbageReport(final PdaCase source) {

    final DecimalFormat df = new DecimalFormat("#,###.00");

    final StringBuilder builder = new StringBuilder();
    builder.append("1. Without additional shifting or any other Owners expenses");
    builder.append(System.lineSeparator());
    builder.append("2. Garbage handling fee is compulsory and includes removal of foll garbage:");
    builder.append(System.lineSeparator());
    builder.append(
        String.format(
            "- Sludge, bilge, waste oils: %s cub.m.",
            df.format(source.getProformaDisbursementAccount().getFreeSweageDisposalQuantity())));
    builder.append(System.lineSeparator());
    builder.append(
        String.format(
            "- Dry garbage, bags not larger than 0.0625 cub.m: %s bags",
            df.format(source.getProformaDisbursementAccount().getFreeGarbageDisposalQuantity())));
    builder.append(System.lineSeparator());
    builder.append("Disposal of slop, sewage and ballast waters not included");

    return getParagraphWithTextJustified(getText(builder.toString(), getRegularFont(), 10))
        .setBackgroundColor(getColorGreen())
        .setPaddings(0, 5, 0, 5);
  }

  //  private static Table getLegendTable() {
  //
  //    Table legendTable = new Table(2);
  //
  //    addLegendRow(
  //        "Due is either fixed or not modified by warnings, or fee is without discount",
  //        getChekMarkSymbol(),
  //        legendTable);
  //    addLegendRow("Due increase can not be evaluated", getQuestionMarkSymbol(), legendTable);
  //    addLegendRow("Due is increased", getArrowUpSymbol(), legendTable);
  //    addLegendRow("Fee with discount", getArrowDownSymbol(), legendTable);
  //    addLegendRow(
  //        "If two status icons are present: first is for ETA, second is for ETD",
  //        new Text(""),
  //        legendTable);
  //
  //    return legendTable;
  //  }
  //
  //  private static void addLegendRow(String explanation, Text statusIcon, Table table) {
  //    table
  //        .startNewRow()
  //        .addCell(
  //            getCellWithNoBorder()
  //                .add(getParagraphWithTextCentered(statusIcon))
  //                .setPaddings(0, 5, 0, 5))
  //        .addCell(
  //            getCellWithNoBorder()
  //                .add(getParagraphWithTextJustified(getText(explanation, getRegularFont(), 10)))
  //                .setPaddings(0, 5, 0, 5))
  //        .setBackgroundColor(getColorGreen());
  //  }
}
