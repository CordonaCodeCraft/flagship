package flagship.domain.renders.pda.elements;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.text.pdf.BaseFont;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.TextAlignment.*;

public abstract class PdaElementsFactory {

  protected static final String FONT_REGULAR = "src/main/resources/fonts/Muller-Regular.otf";
  protected static final String FONT_ITALIC = "src/main/resources/fonts/Muller-Regular Italic.otf";
  protected static final String FONT_BOLD = "src/main/resources/fonts/Muller-Bold.otf";
  protected static final String FONT_CHARACTER = "c:/windows/fonts/arialuni.ttf";

  protected static final String ARROW_DOWN_SYMBOL = "\u2193";
  protected static final String ARROW_UP_SYMBOL = "\u2191";
  protected static final String CHECKMARK_SYMBOL = "\u2713";
  protected static final String QUESTION_MARK_SYMBOL = "\uFF1F";

  protected Document getNewDocument(final String filePath) throws FileNotFoundException {
    final OutputStream fos = new FileOutputStream(filePath);
    final PdfWriter writer = new PdfWriter(fos);
    final PdfDocument pdf = new PdfDocument(writer);
    final PageSize pageSize = PageSize.A4;
    final Document document = new Document(pdf, pageSize);
    document.setMargins(10, 10, 10, 10);
    return document;
  }

  protected static Table getOuterTable() {
    final Table table = new Table(2);
    final Cell left = new Cell().setWidth(PageSize.A4.getWidth() / 2).setBorder(NO_BORDER);
    final Cell right = new Cell().setWidth(PageSize.A4.getWidth() / 2).setBorder(NO_BORDER);
    return table.addCell(left).addCell(right);
  }

  protected static Table getInnerTable() {
    final Table table = new Table(2);
    final Cell left = new Cell().setWidth(PageSize.A4.getWidth() / 4).setBorder(NO_BORDER);
    final Cell right = new Cell().setWidth(PageSize.A4.getWidth() / 4).setBorder(NO_BORDER);
    return table.addCell(left).addCell(right).useAllAvailableWidth();
  }

  protected static Cell getCellWithNoBorder() {
    return new Cell().setBorder(NO_BORDER);
  }

  protected static Cell getCellWithNoBorder(int width) {
    return new Cell().setWidth(width).setBorder(NO_BORDER);
  }

  protected static Cell getCellWithNoBorder(int rowspan, int colspan) {
    return new Cell(rowspan, colspan).setBorder(NO_BORDER);
  }

  protected static Cell getCellWithRightBorder() {
    return new Cell().setBorder(NO_BORDER).setBorderRight(new SolidBorder(1.0f));
  }

  protected static Cell getCellWithRightBorder(int width) {
    return new Cell().setBorder(NO_BORDER).setBorderRight(new SolidBorder(1.0f)).setWidth(width);
  }

  protected static Cell getCellWithBottomBorder(int rowspan, int colspan, int width) {
    return new Cell(rowspan, colspan)
        .setBorder(NO_BORDER)
        .setBorderBottom(new SolidBorder(1.0f))
        .setWidth(width);
  }

  protected static Cell getCellWithBottomBorder(int width) {
    return new Cell().setBorder(NO_BORDER).setBorderBottom(new SolidBorder(1.0f)).setWidth(width);
  }

  protected static Cell getCellWithBottomBorder() {
    return new Cell().setBorder(NO_BORDER).setBorderBottom(new SolidBorder(1.0f));
  }

  protected static Paragraph getParagraphWithTextCentered(final Text text) {
    return new Paragraph().add(text).setTextAlignment(CENTER);
  }

  protected static Paragraph getParagraphWithTextAlignedRight(final Text text) {
    return new Paragraph().add(text).setTextAlignment(RIGHT);
  }

  protected static Paragraph getParagraphWithTextAlignedLeft(final Text text) {
    return new Paragraph().add(text).setTextAlignment(LEFT);
  }

  protected static Paragraph getParagraphWithImageAlignedRight(final Image image) {
    return new Paragraph().add(image).setTextAlignment(RIGHT);
  }

  protected static Paragraph getParagraphWithImageCentered(final Image image) {
    return new Paragraph().add(image).setTextAlignment(CENTER);
  }

  protected static PdfFont getRegularFont()  {
    try {
      return PdfFontFactory.createFont(
          FONT_REGULAR, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  protected PdfFont getItalicFont() throws IOException {
    return PdfFontFactory.createFont(FONT_ITALIC, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected static PdfFont getBoldFont() throws IOException {
    return PdfFontFactory.createFont(FONT_BOLD, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected static PdfFont getCharFont() throws IOException {
    return PdfFontFactory.createFont(
        FONT_CHARACTER, BaseFont.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected static Text getText(final String text, final PdfFont font, final int fontSize) {
    return new Text(text).setFont(font).setFontSize(fontSize);
  }

  protected static Text getQuestionMarkSymbol() {
    try {
      return getText(QUESTION_MARK_SYMBOL, getCharFont(), 10)
          .setBold()
          .setFontColor(new DeviceRgb(100, 40, 0));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  protected static Text getArrowDownSymbol()  {
    try {
      return getText(ARROW_DOWN_SYMBOL, getCharFont(), 10)
          .setBold()
          .setFontColor(new DeviceRgb(0, 105, 148));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  protected static Text getArrowUpSymbol() {
    try {
      return getText(ARROW_UP_SYMBOL, getCharFont(), 10)
          .setBold()
          .setFontColor(new DeviceRgb(110, 10, 30));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  protected static Text getChekMarkSymbol() {
    try {
      return getText(CHECKMARK_SYMBOL, getCharFont(), 10)
          .setBold()
          .setFontColor(new DeviceRgb(120, 190, 33));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public enum DueType {
    TONNAGE_DUE("Tonnage due"),
    WHARF_DUE("Wharf due"),
    CANAL_DUE("Canal due"),
    LIGHT_DUE("Light due"),
    MARPOL_DUE("Marpol due"),
    BOOM_CONTAINMENT_DUE("Boom containment due"),
    SAILING_PERMISSION_DUE("Sailing permission"),
    PILOTAGE_DUE("Pilotage"),
    TUG_DUE("Tug services"),
    MOORING_DUE("Mooring & Unmooring"),
    BASIC_AGENCY_DUE("Basic agency fee"),
    CARS_DUE("Cars"),
    CLEARANCE_DUE("Clearance"),
    COMMUNICATIONS_DUE("Communications"),
    BANK_EXPENSES("Bank expenses"),
    OVERTIME_DUE("Agency overtime");

    public final String type;

    DueType(String name) {
      this.type = name;
    }
  }
}
