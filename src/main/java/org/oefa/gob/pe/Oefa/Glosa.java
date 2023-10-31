package org.oefa.gob.pe.Oefa;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.oefa.gob.pe.Oefa.common.Constant;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import org.oefa.gob.pe.Oefa.domain.GlosaPeru;
import org.oefa.gob.pe.Oefa.domain.GlosaOefa;

import java.io.ByteArrayOutputStream;


public class Glosa {


    /**
     * Función que se encarga de agregar la glosa de verificación del servicio SSFD a un archivo PDF.
     * @param bytesFile Bytes del archivo al que se le agregará la glosa.
     * @param glosa Objeto que contiene la información de la glosa.
     * @return Bytes del nuevo archivo que incluye la glosa.
     * @throws Exception
     */
     public static byte[] generateGlosaOefa(byte[] bytesFile, GlosaOefa glosa) throws Exception{
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

         float posX = (pageSize.getWidth() - Constant.GlosaOefa.WIDTH) / 2;
         float posY = (pageSize.getHeight() - Constant.GlosaOefa.HEIGHT);
         int margin = Constant.GlosaOefa.MARGIN;

         Dimension containerDimension = new Dimension(posX, posY-margin, Constant.GlosaOefa.WIDTH, Constant.GlosaOefa.HEIGHT, 1);
         Dimension qrDimension = new Dimension(posX + margin, posY - margin + 3, 100, 100, 64.0f );
         Dimension barcodeDimension = new Dimension(posX + 100, posY - margin + 5, 0, 0, 75.0f);

         Image qrImage = Qr.generateQr(qrDimension, glosa.getUrl());
         Image barcodeImage = Barcode.generateBarcode(pcb, barcodeDimension, glosa.getBarcodePin());
         Rectangle container = new Rectangle(
                 containerDimension.getX(),
                 containerDimension.getY(),
                 containerDimension.getX() + containerDimension.getWidth(),
                 containerDimension.getY() + containerDimension.getHeight()
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

         float left = containerDimension.getX() + qrDimension.getWidth();
         float bottom = containerDimension.getY() + margin;
         float right = containerDimension.getX() + containerDimension.getWidth() - margin;
         float top = containerDimension.getY() + containerDimension.getHeight() - margin /2f;

         ColumnText ct = new ColumnText(pcb);
         ct.setAlignment(Element.ALIGN_LEFT);
         ct.setLeading(0.0f, 1.0f);
         ct.addText(phrase);
         ct.setSimpleColumn(left, bottom, right, top);
         ct.go();

         writer.close();
         bout.close();
         document.close();
         reader.close();

         return bout.toByteArray();

    }


    /**
     * Función que se encargará de agregar la glosa de verificación vertical de Firma Perú a un archivo PDF.
     * @param fileBytes Bytes del archivo al que se le agregará la glosa.
     * @param glosaPeru Objeto que contiene la información de la glosa.
     * @return Bytes del nuevo archivo que incluye la glosa
     * @throws Exception
     */
    public static byte[] generateGlosaFirmaPeru(byte[] fileBytes, GlosaPeru glosaPeru) throws Exception {
        PdfReader reader = new PdfReader(fileBytes);
        PdfReader.unethicalreading = true;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bout);

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte pdfContentByte = stamper.getOverContent(i);
            boolean horizontalPage = false;

            PdfGState gState = new PdfGState();
            gState.setFillOpacity(Constant.GlosaFirmaPeru.OPACITY_TEXT);
            pdfContentByte.setGState(gState);

            Rectangle rectangle = reader.getPageSize(i);
            if(rectangle.getWidth() > rectangle.getHeight())
                horizontalPage = true;

            float posX1 = rectangle.getWidth() - 30;
            float posX2 = rectangle.getWidth() - 18;
            float posY1 = ( rectangle.getHeight() - (horizontalPage ? Constant.GlosaFirmaPeru.HEIGHT_TEXT_TOP_HORIZONTAL : Constant.GlosaFirmaPeru.HEIGHT_TEXT_TOP_VERTICAL) ) / 2;
            float posY2 = ( rectangle.getHeight() - (horizontalPage ? Constant.GlosaFirmaPeru.HEIGHT_TEXT_BOTTOM_HORIZONTAL: Constant.GlosaFirmaPeru.HEIGHT_TEXT_BOTTOM_VERTICAL) ) / 2;
            float posY3 = posY2 + (horizontalPage ? 298 : 372);
            float fontSize = horizontalPage ? Constant.GlosaFirmaPeru.FONT_HORIZONTAL : Constant.GlosaFirmaPeru.FONT_VERTICAL;
            float rotation = 90;

            Rectangle rect = new Rectangle(posX1-14, posY1-5, posX1+20, posY1+5+(horizontalPage ? Constant.GlosaFirmaPeru.HEIGHT_TEXT_TOP_HORIZONTAL : Constant.GlosaFirmaPeru.HEIGHT_TEXT_TOP_VERTICAL));
            rect.setBorderColor(new BaseColor(89, 89, 89, Constant.GlosaFirmaPeru.OPACITY_RECTANGLE));
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(0.5f);
            pdfContentByte.rectangle(rect);

            pdfContentByte.beginText();
            pdfContentByte.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_OBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), fontSize);
            pdfContentByte.setColorFill(new BaseColor(89, 89, 89));
            pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getTopLine(), posX1, posY1, rotation);
            pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getBottomLine(), posX2, posY2, rotation);

            pdfContentByte.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA_BOLDOBLIQUE, BaseFont.CP1252, BaseFont.NOT_EMBEDDED), fontSize);
            pdfContentByte.setColorFill(new BaseColor(23, 64, 196));
            pdfContentByte.showTextAligned(Element.ALIGN_LEFT, glosaPeru.getUrl(), posX2, posY3, rotation);

            pdfContentByte.endText();
        }

        stamper.close();
        bout.close();
        reader.close();

        return bout.toByteArray();

    }

}
