package org.oefa.gob.pe.util;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import org.junit.jupiter.api.Test;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import org.oefa.gob.pe.Oefa.Glosa;
import org.oefa.gob.pe.Oefa.domain.GlosaSSFD;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


class GlosaUtilTest {

    private final String testFilePath = "C://Users/71489974/Desktop/Prueba_5.pdf";
    private final String destFilePath = "C://Users/71489974/Desktop/Prueba_5_glosa.pdf";
    private final String text = "Documento electrónico firmado digitalmente en el marco de la Ley N° 27269, Ley de Firmas y Certificados Digitales, su Reglamento y modificatorias. %%La integridad del documento y la autoría de la(s) firma(s) pueden ser verificadas en %%https://apps.firmaperu.gob.pe/web/validador.xhtml";
    private final String url = "https://apps.firmaperu.gob.pe/web/validador.xhtml";


    @Test
    void generarVerificacion() throws Exception {
        // GIVEN
        byte[] fileBytes = Files.readAllBytes(Paths.get(this.testFilePath));
        GlosaSSFD glosa = new GlosaSSFD(text, url, "5435345");

        Rectangle pageSize = PageSize.A4;
        float x = (pageSize.getWidth() - 500) / 2;
        float y = (pageSize.getHeight() - 70);
        int margin = 20;
        Dimension containerDimension = new Dimension(x, y - margin, 500, 70, 1);
        Dimension qrDimension = new Dimension(x + margin, y - margin + 3, 100, 100, 64.0f);
        Dimension barcodeDimension = new Dimension(x + 100, y - margin + 5, 0, 0, 75.0f);

        // WHEN
        Glosa.MARGIN = margin;
        byte[] bytesOut = Glosa.generateGlosa(fileBytes, glosa, containerDimension, qrDimension, barcodeDimension);
        FileOutputStream fout = new FileOutputStream(this.destFilePath);
        fout.write(bytesOut);
        fout.close();

        // THEN
        assert bytesOut.length > 0;

    }


    @Test
    public void addGlosaVertical() throws Exception {
        /*
        // GIVEN
        byte[] fileBytes = Files.readAllBytes(Paths.get(this.testFilePath));

        // WHEN
        byte[] bytesOut = Glosa.generateGlosaVertical(fileBytes,text);
        FileOutputStream fout = new FileOutputStream(this.destFilePath);
        fout.write(bytesOut);
        fout.close();

        // THEN
        assert bytesOut.length > 0;

         */

    }



}