package flagship.domain;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;

public abstract class ITextElementsFactory {

  protected Cell getCell(BorderDesign borderDesign) {

    switch (borderDesign) {
      case WITHOUT_BORDER:
        return new Cell().setBorder(Border.NO_BORDER);
      default:
        return new Cell();
    }
  }

  protected Paragraph getParagraph(TextDirection direction) {
    switch (direction) {
      case LEFT_ALIGNMENT:
        return new Paragraph().setTextAlignment(com.itextpdf.layout.property.TextAlignment.LEFT);
      case RIGHT_ALIGNMENT:
        return new Paragraph().setTextAlignment(com.itextpdf.layout.property.TextAlignment.RIGHT);
      case CENTERED:
        return new Paragraph().setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER);
      default:
        return new Paragraph();
    }
  }

  protected Paragraph getParagraph() {
    return new Paragraph();
  }

  protected Cell getCell() {
    return new Cell();
  }

  enum BorderDesign {
    WITHOUT_BORDER,
  }

  enum TextDirection {
    LEFT_ALIGNMENT,
    RIGHT_ALIGNMENT,
    NO_ALIGNMENT,
    CENTERED
  }
}
