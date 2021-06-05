package flagship.domain.renders.pda.elements;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;

@Slf4j
public class LogoRender extends PdaElementsFactory {

  private static final String LOGO_PATH = "src/main/resources/images/logo.png";

  public static Cell renderLogo() throws MalformedURLException {
    final Image logo = new Image(ImageDataFactory.create(LOGO_PATH));
    final Paragraph logoParagraph = getParagraphWithImageCentered(logo).setMarginTop(3);
    log.info("Rendered logo");
    return getCellWithNoBorder().add(logoParagraph);
  }
}
