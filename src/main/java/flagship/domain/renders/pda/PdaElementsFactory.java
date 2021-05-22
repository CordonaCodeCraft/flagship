package flagship.domain.renders.pda;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.property.TextAlignment.LEFT;
import static com.itextpdf.layout.property.TextAlignment.RIGHT;

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
    final Cell left = new Cell().setBorder(NO_BORDER);
    final Cell right = new Cell().setBorder(NO_BORDER);
    return table.addCell(left).addCell(right).useAllAvailableWidth();
  }

  protected static Table getInnerTable() {
    final Table table = new Table(2);
    final Cell left = new Cell().setBorder(NO_BORDER);
    final Cell right = new Cell().setBorder(NO_BORDER);
    return table.addCell(left).addCell(right).useAllAvailableWidth();
  }

  protected Cell getCell() {
    return new Cell().setBorder(NO_BORDER);
  }

  protected Cell getCell(int rowspan, int colspan) {
    return new Cell(rowspan, colspan).setBorder(NO_BORDER);
  }

  protected PdfFont getRegularFont() throws IOException {
    return PdfFontFactory.createFont(
        FONT_REGULAR, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected PdfFont getItalicFont() throws IOException {
    return PdfFontFactory.createFont(FONT_ITALIC, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected PdfFont getBoldFont() throws IOException {
    return PdfFontFactory.createFont(FONT_BOLD, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
  }

  protected Paragraph getParagraphTextAlignedRight(final Text text) {
    return new Paragraph().add(text).setTextAlignment(RIGHT);
  }

  protected Paragraph getParagraphTextAlignedLeft(final Text text) {
    return new Paragraph().add(text).setTextAlignment(LEFT);
  }

  protected Paragraph getParagraphImageAlignedRight(final Image image) {
    return new Paragraph().add(image).setTextAlignment(RIGHT);
  }

  protected Text getText(final String text, final PdfFont font, final int fontSize) {
    return new Text(text).setFont(font).setFontSize(fontSize);
  }
}
