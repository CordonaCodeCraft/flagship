package flagship.domain;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import flagship.domain.cases.dto.PdaCase;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.itextpdf.layout.borders.Border.NO_BORDER;
import static com.itextpdf.layout.borders.Border.SOLID;
import static com.itextpdf.layout.property.TextAlignment.LEFT;
import static com.itextpdf.layout.property.TextAlignment.RIGHT;

@Component
@Slf4j
@Setter
@AllArgsConstructor
public class PdaRenderer extends ITextElementsFactory {

  private PdaCase source;
  private static final String LOGO_PATH = "D:/Gdrive/Inbox/neftoil.png";
  private static final String DESTINATION = "D:/Gdrive/Inbox/";

  public void renderPda() throws FileNotFoundException, MalformedURLException {

    final String filePath = DESTINATION + getFileName(source);

//    Document pda = getDocument(filePath);
//    pda.add(renderHead());
//    pda.add(renderDetails());
//    pda.add(renderFinancials());
//    pda.close();
  }

  private Table renderFinancials() {
    Table table = new Table(2);
    Cell calculations = renderCalculations();
    Cell information = renderInformation();

    return table.addCell(calculations).addCell(information);
  }

  private Cell renderCalculations() {

    Paragraph title =
        new Paragraph()
            .add(new Text("Dues").setBold().setFontSize(12))
            .setTextAlignment(RIGHT)
            .setMarginRight(6);

    Cell number =
        new Cell()
            .add(new Paragraph(new Text("No.").setBold()).setTextAlignment(RIGHT))
            .setWidth(30)
            .setBorder(NO_BORDER)
            .setBorderBottom(new SolidBorder(SOLID));

    Cell item =
        new Cell()
            .add(new Paragraph(new Text("Due type").setBold()).setTextAlignment(RIGHT))
            .setWidth(150)
            .setBorder(NO_BORDER)
            .setBorderBottom(new SolidBorder(SOLID));

    Cell eur =
        new Cell()
            .add(new Paragraph(new Text("Amount").setBold()).setTextAlignment(RIGHT))
            .setWidth(80)
            .setBorder(NO_BORDER)
            .setBorderBottom(new SolidBorder(SOLID));

    Table table = new Table(3);

    table.addCell(number);
    table.addCell(item);
    table.addCell(eur);

    populateCalculations(table);

    return new Cell().add(title).add(table);
  }

  private void populateCalculations(final Table table) {

    Arrays.stream(source.getProformaDisbursementAccount().getClass().getDeclaredFields())
        .filter(field -> field.getName().contains("Due"))
        .forEach(field -> populateTable(field, table));
  }

  private void populateTable(final Field field, final Table table) {


  }



  private Cell renderInformation() {

    Paragraph title =
        new Paragraph().add(new Text("Notes").setBold().setFontSize(12)).setTextAlignment(RIGHT);

    return new Cell().add(title);
  }

  private Table renderHead() throws MalformedURLException {

    String issueDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MM yyyy"));

    Div div = new Div();
    div.add(new Paragraph().add(new Text("Proforma Disbursement Account").setFontSize(15)));
    div.add(new Paragraph().add(new Text(String.format("Issued on %s", issueDate))));
    div.setTextAlignment(RIGHT);
    div.setBold();

    Cell left = new Cell().setBorder(NO_BORDER);
    left.add(div);

    Paragraph logo =
        new Paragraph().add(new Image(ImageDataFactory.create(LOGO_PATH))).setMarginTop(5);

    Cell right = new Cell().setPaddingLeft(15).setBorder(NO_BORDER);
    right.add(logo);

    Table table = new Table(2);

    table.addCell(left).addCell(right);

    return table;
  }

  private Paragraph renderDetails() throws MalformedURLException {
    Paragraph head = new Paragraph();
    Table table = new Table(2);
    table.addCell(getLeftSide().setPaddingLeft(15).setBorder(NO_BORDER));
    table.addCell(getRightSide().setPaddingLeft(15).setBorder(NO_BORDER));
    head.add(table);
    return head;
  }

  private Cell getLeftSide() throws MalformedURLException {

    Paragraph shipParticulars =
        new Paragraph()
            .add(new Text("Ship particulars").setBold().setUnderline())
            .setTextAlignment(RIGHT);

    Paragraph nameLabel = new Paragraph().add(new Text("Name").setBold()).setTextAlignment(LEFT);
    Paragraph typeLabel = new Paragraph().add(new Text("Type").setBold()).setTextAlignment(LEFT);
    Paragraph gtLabel = new Paragraph().add(new Text("GT").setBold()).setTextAlignment(LEFT);
    Paragraph loaLabel = new Paragraph().add(new Text("L.O.A").setBold()).setTextAlignment(LEFT);

    Paragraph nameValue =
        new Paragraph().add(new Text(source.getShip().getName())).setTextAlignment(RIGHT);

    Paragraph typeValue =
        new Paragraph().add(new Text(source.getShip().getType().type)).setTextAlignment(RIGHT);

    Paragraph gtValue =
        new Paragraph().add(source.getShip().getGrossTonnage() + " mt").setTextAlignment(RIGHT);

    Paragraph loaValue =
        new Paragraph()
            .add(source.getShip().getGrossTonnage().intValue() + " m")
            .setTextAlignment(RIGHT);

    Paragraph callDetails =
        new Paragraph()
            .add(new Text("Call details").setBold().setUnderline())
            .setTextAlignment(RIGHT);

    Paragraph callCountLabel =
        new Paragraph().add(new Text("No. of call").setBold()).setTextAlignment(LEFT);

    Paragraph callPurposeLabel =
        new Paragraph().add(new Text("Purpose of call").setBold()).setTextAlignment(LEFT);

    Paragraph alongsideDaysLabel =
        new Paragraph().add(new Text("Vessel alongside for").setBold()).setTextAlignment(LEFT);

    Paragraph callCountValue =
        new Paragraph()
            .add(new Text(String.valueOf(source.getCallCount())))
            .setTextAlignment(RIGHT);

    Paragraph callPurposeValue =
        new Paragraph().add(new Text(source.getCallPurpose().type)).setTextAlignment(RIGHT);

    Paragraph alongsideDaysValue =
        new Paragraph()
            .add(new Text(String.valueOf(source.getAlongsideDaysExpected()) + " days"))
            .setTextAlignment(RIGHT);

    Table table = new Table(2);
    table.useAllAvailableWidth();

    table.addCell(new Cell(1, 2).add(shipParticulars).setBorder(NO_BORDER));
    table.addCell(new Cell().add(nameLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(nameValue).setBorder(NO_BORDER));
    table.addCell(new Cell().add(typeLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(typeValue).setBorder(NO_BORDER));
    table.addCell(new Cell().add(gtLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(gtValue).setBorder(NO_BORDER));
    table.addCell(new Cell().add(loaLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(loaValue).setBorder(NO_BORDER));
    table.addCell(new Cell(1, 2).add(callDetails).setBorder(NO_BORDER));
    table.addCell(new Cell().add(callCountLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(callCountValue).setBorder(NO_BORDER));
    table.addCell(new Cell().add(callPurposeLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(callPurposeValue).setBorder(NO_BORDER));
    table.addCell(new Cell().add(alongsideDaysLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(alongsideDaysValue).setBorder(NO_BORDER));

    Paragraph cargoManifest =
        new Paragraph()
            .add(new Text("Cargo manifest").setBold().setUnderline())
            .setTextAlignment(RIGHT);

    Paragraph cargosList =
        new Paragraph().add(String.join(", ", source.getCargoManifest())).setTextAlignment(RIGHT);

    return new Cell().add(table).add(cargoManifest).add(cargosList);
  }

  private Cell getRightSide() {

    Paragraph companyDetails =
        new Paragraph()
            .add(new Text("Company details").setBold().setUnderline())
            .setTextAlignment(RIGHT);

    Paragraph addressLabel =
        new Paragraph().add(new Text("Address").setBold()).setTextAlignment(LEFT);

    Paragraph mobileLabel =
        new Paragraph().add(new Text("Mobile").setBold()).setTextAlignment(LEFT);

    Paragraph faxLabel = new Paragraph().add(new Text("Fax").setBold()).setTextAlignment(LEFT);

    Paragraph emailFirstLabel =
        new Paragraph().add(new Text("Email").setBold()).setTextAlignment(LEFT);

    Paragraph emailSecondLabel =
        new Paragraph().add(new Text("Email").setBold()).setTextAlignment(LEFT);

    Paragraph companyData =
        new Paragraph().add(new Text("Patron Logistics LTD")).setTextAlignment(RIGHT);

    Paragraph addressData =
        new Paragraph()
            .add(new Text("Varna, 35 Patron Street, entr. 2, floor 3, app.5"))
            .setTextAlignment(RIGHT);

    Paragraph mobileData = new Paragraph().add(new Text("+359 899234345")).setTextAlignment(RIGHT);

    Paragraph faxData = new Paragraph().add(new Text("+3592987672")).setTextAlignment(RIGHT);

    Paragraph emailFirstData =
        new Paragraph().add(new Text("patronInfo@patronlogistics.eu")).setTextAlignment(RIGHT);
    Paragraph emailSecondData =
        new Paragraph().add(new Text("patronOffice@patronlogistics.eu")).setTextAlignment(RIGHT);

    Paragraph paymentDetails =
        new Paragraph()
            .add(new Text("Payment details").setBold().setUnderline())
            .setTextAlignment(RIGHT);

    Paragraph beneficiaryLabel =
        new Paragraph().add(new Text("Beneficiary")).setBold().setTextAlignment(LEFT);

    Paragraph bankNameLabel =
        new Paragraph().add(new Text("Bank").setBold()).setTextAlignment(LEFT);

    Paragraph swiftCodeLabel =
        new Paragraph().add(new Text("SWIFT (BIC) code").setBold()).setTextAlignment(LEFT);

    Paragraph bankAccountLabel =
        new Paragraph().add(new Text("IBAN (Euro)").setBold()).setTextAlignment(LEFT);

    Paragraph beneficiaryData =
        new Paragraph().add(new Text("Patron Logistics LTD")).setTextAlignment(RIGHT);

    Paragraph bankNameData = new Paragraph().add(new Text("Patron Bank")).setTextAlignment(RIGHT);

    Paragraph swiftCodeData = new Paragraph().add(new Text("PATBGSF")).setTextAlignment(RIGHT);

    Paragraph bankAccountData =
        new Paragraph().add(new Text("BG99PAT999888777")).setTextAlignment(RIGHT);

    Table table = new Table(2);

    table.useAllAvailableWidth();

    table.addCell(new Cell(1, 2).add(companyDetails).setBorder(NO_BORDER));
    table.addCell(new Cell().add(addressLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(addressData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(mobileLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(mobileData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(faxLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(faxData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(emailFirstLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(emailFirstData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(emailSecondLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(emailSecondData).setBorder(NO_BORDER));
    table.addCell(new Cell(1, 2).add(paymentDetails).setBorder(NO_BORDER));
    table.addCell(new Cell().add(beneficiaryLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(beneficiaryData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(bankNameLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(bankNameData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(swiftCodeLabel).setWidth(60).setBorder(NO_BORDER));
    table.addCell(new Cell().add(swiftCodeData).setBorder(NO_BORDER));
    table.addCell(new Cell().add(bankAccountLabel).setBorder(NO_BORDER));
    table.addCell(new Cell().add(bankAccountData).setBorder(NO_BORDER));

    return new Cell().add(table);
  }



  private String getFileName(final PdaCase source) {
    return String.format("%s_%s.pdf", source.getShip().getName(), LocalDate.now().toString());
  }


}
