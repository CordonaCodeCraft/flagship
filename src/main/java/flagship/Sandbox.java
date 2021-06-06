package flagship;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import flagship.domain.cases.dto.PdaCase;
import flagship.domain.cases.dto.PdaPort;
import flagship.domain.cases.dto.PdaShip;
import flagship.domain.cases.dto.mappers.CaseMapper;
import flagship.domain.cases.entities.Case;
import flagship.domain.tariffs.servicedues.TugDueTariff;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;

import static flagship.domain.cases.entities.Case.CallPurpose.LOADING;
import static flagship.domain.cases.entities.Ship.ShipType.BULK_CARRIER;
import static flagship.domain.tariffs.PortArea.FIRST;
import static flagship.domain.tariffs.servicedues.PilotageDueTariff.PilotageArea.VARNA_FIRST;

public class Sandbox {

  public static final String LOGO = "D:/Gdrive/Inbox/neftoil.png";
  public static final String DEST = "D:/Gdrive/Inbox/test.pdf";

  public static void main(String[] args) throws JSONException, IOException {

    PdfWriter writer = new PdfWriter(DEST);
    PdfDocument pdf = new PdfDocument(writer);

    String code = "\u2714";
    System.out.println(code);

    Document document = new Document(pdf);
    document.setMargins(5, 5, 5, 5);

    composeHead(document);
    composeShipData(document);

    document.close();

    //        dtoConversions();
  }

  private static void composeHead(final Document document) throws MalformedURLException {

    //    firstCell.setBorder(Border.NO_BORDER);

    //    secondCell.setBorder(Border.NO_BORDER);

  }

  private static void composeShipData(final Document document) {
    Paragraph paragraph1 = new Paragraph();
    paragraph1
        .add("Ship's name: Millennium's Falcon")
        .setTextAlignment(TextAlignment.LEFT)
        .setPaddingLeft(130);

    Paragraph paragraph2 = new Paragraph();
    paragraph2
        .add("Ship's type: Star destroyer raper")
        .setTextAlignment(TextAlignment.LEFT)
        .setPaddingLeft(135);

    document.add(paragraph1);
    document.add(paragraph2);
  }

  private static void dtoConversions() {
    PdaShip ship =
        PdaShip.builder()
            .name("Aura")
            .type(BULK_CARRIER)
            .lengthOverall(BigDecimal.valueOf(156.25))
            .grossTonnage(BigDecimal.valueOf(2500))
            .hasIncreasedManeuverability(true)
            .build();

    PdaPort port =
        PdaPort.builder()
            .name("Varna")
            .portArea(FIRST)
            .pilotageArea(VARNA_FIRST)
            .tugArea(TugDueTariff.TugArea.VTC_FIFTH)
            .build();

    PdaCase pdaCase =
        PdaCase.builder()
            .ship(ship)
            .port(port)
            .callPurpose(LOADING)
            .callCount(5)
            .alongsideDaysExpected(10)
            .estimatedDateOfArrival(LocalDate.of(2021, 5, 10))
            .estimatedDateOfDeparture(LocalDate.of(2021, 6, 15))
            .build();

    Case newCase = CaseMapper.INSTANCE.pdaCaseToCase(pdaCase);

    System.out.println();
  }
}
