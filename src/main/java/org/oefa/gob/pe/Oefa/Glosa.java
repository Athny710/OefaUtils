package org.oefa.gob.pe.Oefa;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import org.oefa.gob.pe.Oefa.domain.GlosaPeru;
import org.oefa.gob.pe.Oefa.domain.GlosaSSFD;

import java.io.ByteArrayOutputStream;


public class Glosa {


    public static int MARGIN;


    /**
     * Función que se encarga de agregar la glosa de verificación del servicio SSFD a un archivo PDF.
     * @param bytesFile Bytes del archivo al que se le agregará la glosa.
     * @param glosa Objeto que contiene la información de la glosa.
     * @param dimensionRectangle Objeto de dimensiones del rectángulo conteneder de la glosa.
     * @param dimensionQr Objeto de dimensiones de la imagen QR.
     * @param dimensionBarcode Objeto de dimensiones de la imagen del código de barras.
     * @return Bytes del nuevo archivo que incluye la glosa.
     * @throws Exception
     */
     public static byte[] generateGlosa(byte[] bytesFile, GlosaSSFD glosa, Dimension dimensionRectangle, Dimension dimensionQr, Dimension dimensionBarcode) throws Exception{
         PdfReader reader = new PdfReader(bytesFile, null);
         PdfReader.unethicalreading = true;
         int pages = reader.getNumberOfPages();

         Document document = new Document(PageSize.A4);
         Rectangle pageSize = document.getPageSize();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, bout);
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        document.open();

        PdfContentByte pcb = writer.getDirectContent();
        for(int i=1; i < pages+1; i++){
            PdfImportedPage page = writer.getImportedPage(reader, i);
            document.setPageSize(reader.getPageSize(i));
            document.newPage();
            pcb.addTemplate(page, 0.0f, 0.0f);
        }
        document.setPageSize(pageSize);
        document.newPage();
        writer.setPageEmpty(false);

        Image qrImage = Qr.generateQr(dimensionQr, glosa.getUrl());
        Image barcodeImage = Barcode.generateBarcode(pcb, dimensionBarcode, glosa.getBarcodePin());

        Rectangle container = new Rectangle(
                dimensionRectangle.getX(),
                dimensionRectangle.getY(),
                dimensionRectangle.getX() + dimensionRectangle.getWidth(),
                dimensionRectangle.getY() + dimensionRectangle.getHeight()
        );
        container.setBorder(15);
        container.setBorderWidth(1.0f);
        container.setBorderColor(BaseColor.BLACK);

        pcb.rectangle(container);
        pcb.addImage(qrImage);
        pcb.addImage(barcodeImage);

        Font textFont = new Font(Font.FontFamily.HELVETICA, 6, 1, BaseColor.BLACK);
        Phrase phrase = new Phrase();
        Chunk chunk = new Chunk(glosa.getText(), textFont);
        chunk.setTextRise(6.0f);
        phrase.add(chunk);
        phrase.setLeading(1.0f);

        float left = dimensionRectangle.getX() + dimensionQr.getWidth();
        float bottom = dimensionRectangle.getY() + MARGIN;
        float right = dimensionRectangle.getX() + dimensionRectangle.getWidth() - MARGIN;
        float top = dimensionRectangle.getY() + dimensionRectangle.getHeight() - MARGIN /1.5f;

        ColumnText ct = new ColumnText(pcb);
        ct.setAlignment(Element.ALIGN_LEFT);
        ct.setLeading(0.0f, 1.0f);
        ct.addText(phrase);
        ct.setSimpleColumn(left, bottom, right, top);
        ct.go();

        //bout.close();
         writer.close();
         bout.close();
         document.close();
         reader.close();

        return bout.toByteArray();

    }


    /**
     * Función que se encarga de agregar la glosa de verificación vertical de Firma Perú a un archivo PDF.
     * @param fileBytes Bytes del archivo al que se le desea agregar la glosa.
     * @param glosaPeru Objeto que contiene la información de la glosa.
     * @return Bytes del nuevo archivo que incluye la glosa.
     * @throws Exception
     */
    public static byte[] generateGlosaVertical(byte[] fileBytes, GlosaPeru glosaPeru) throws Exception {
         PdfReader reader = new PdfReader(fileBytes);
         PdfReader.unethicalreading = true;
         ByteArrayOutputStream bout = new ByteArrayOutputStream();
         PdfStamper stamper = new PdfStamper(reader, bout);

         for (int i = 1; i <= reader.getNumberOfPages(); i++) {
             PdfContentByte pdfContentByte = stamper.getOverContent(i);
             pdfContentByte.beginText();

             Rectangle rectangle = reader.getPageSize(i);
             if(rectangle.getWidth() > rectangle.getHeight()){
                 pdfContentByte.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 8.5f);
                 pdfContentByte.setColorFill(new BaseColor(89, 89, 89));
                 pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getTopLine(), 20, 10, 90);
                 pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getBottomLine(), 32, 10, 90);

                 pdfContentByte.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLDOBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 8.5f);
                 pdfContentByte.setColorFill(new BaseColor(23, 64, 196));
                 pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getUrl(), 32, 335, 90);

             }else{
                 pdfContentByte.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 10f);
                 pdfContentByte.setColorFill(new BaseColor(89, 89, 89));
                 pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getTopLine(), 20, 60, 90);
                 pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getBottomLine(), 32, 60, 90);

                 pdfContentByte.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLDOBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 10f);
                 pdfContentByte.setColorFill(new BaseColor(23, 64, 196));
                 pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getUrl(), 32, 435, 90);

             }
             pdfContentByte.endText();

            }

         stamper.close();
         bout.close();
         reader.close();

        return bout.toByteArray();

    }


}
