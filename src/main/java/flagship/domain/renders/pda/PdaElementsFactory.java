package flagship.domain.renders.pda;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;

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

  protected static Cell getCellWithBottomBorder() {
    return new Cell().setBorder(NO_BORDER).setBorderBottom(new SolidBorder(1.0f));
  }

  protected static Cell getCellWithBottomBorder(int width) {
    return new Cell().setBorder(NO_BORDER).setBorderBottom(new SolidBorder(1.0f)).setWidth(width);
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

  protected static PdfFont getRegularFont() throws IOException {
    return PdfFontFactory.createFont(
        FONT_REGULAR, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected PdfFont getItalicFont() throws IOException {
    return PdfFontFactory.createFont(FONT_ITALIC, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected static PdfFont getBoldFont() throws IOException {
    return PdfFontFactory.createFont(FONT_BOLD, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected static Text getText(final String text, final PdfFont font, final int fontSize) {
    return new Text(text).setFont(font).setFontSize(fontSize);
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
