package org.oefa.gob.pe.util;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import org.junit.jupiter.api.Test;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import org.oefa.gob.pe.Oefa.Glosa;
import org.oefa.gob.pe.Oefa.domain.GlosaPeru;
import org.oefa.gob.pe.Oefa.domain.GlosaOefa;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


class GlosaUtilTest {

    private final String testFilePath = "C://Users/Anthony/Documents/Pruebas/Oficio_v1.pdf";
    private final String destFilePath = "C://Users/Anthony/Desktop/Prueba_glosa.pdf";
    private final String text = "Documento electrónico firmado digitalmente en el marco de la Ley N° 27269, Ley de Firmas y Certificados Digitales, su Reglamento y modificatorias. %%La integridad del documento y la autoría de la(s) firma(s) pueden ser verificadas en %%https://apps.firmaperu.gob.pe/web/validador.xhtml";
    private final String text2 = "Documento electrónico firmado digitalmente en el marco de la Ley N° 27269, Ley de Firmas y Certificados Digitales, su Reglamento y modificatorias. %%https://apps.firmaperu.gob.pe/web/validador.xhtml%%https://apps.firmaperu.gob.pe/web/validador.xhtml";
    private final String url = "https://apps.firmaperu.gob.pe/web/validador.xhtml";


    @Test
    void generarVerificacion() throws Exception {
        // GIVEN
        byte[] fileBytes = Files.readAllBytes(Paths.get(this.testFilePath));
        GlosaOefa glosa = new GlosaOefa(text, url, "5435345");

        // WHEN
        byte[] bytesOut = Glosa.generateGlosaOefa(fileBytes, glosa);
        FileOutputStream fout = new FileOutputStream(this.destFilePath);
        fout.write(bytesOut);
        fout.close();

        // THEN
        assert bytesOut.length > 0;

    }


    @Test
    public void addGlosaVertical() throws Exception {
        // GIVEN
        byte[] fileBytes = Files.readAllBytes(Paths.get(this.testFilePath));
        String[] parts = this.text.split("%%");
        GlosaPeru glosaPeru = new GlosaPeru(parts[0], parts[1], parts[2]);
        GlosaOefa glosaOefa = new GlosaOefa(text, url, "5435345");

        // WHEN
        byte[] bytesout_1 = Glosa.generateGlosaOefa(fileBytes, glosaOefa);
        byte[] bytesout_2 = Glosa.generateGlosaFirmaPeru(bytesout_1,glosaPeru);
        FileOutputStream fout = new FileOutputStream(this.destFilePath);
        fout.write(bytesout_2);
        fout.close();

        // THEN
        assert bytesout_2.length > 0;

    }



}