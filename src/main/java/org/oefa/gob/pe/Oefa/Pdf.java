package org.oefa.gob.pe.Oefa;

import com.aspose.words.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.oefa.gob.pe.Oefa.domain.CharPosition;
import org.oefa.gob.pe.Oefa.util.LoaderUtil;
import org.oefa.gob.pe.Oefa.util.StringUtil;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Pdf {


    public Pdf() throws Exception {
        License license = new License();
        license.setLicense(LoaderUtil.loadResource("SSignerProd.lic"));

    }


    /**
     * Función que se encarga de convertir archivos de tipo DOCX a PDF.
     * @param directoryPath Ubicación del archivo.
     * @param fileName Nombre del archivo.
     * @return Nombre del archivo en formato PDF.
     * @throws Exception
     */
    public String convertToPdf(String directoryPath, String fileName) throws Exception{
        InputStream stream = Files.newInputStream(Paths.get(directoryPath + fileName));
        String pdfName = StringUtil.extractFileName(fileName) + ".pdf";

        Document doc = new Document(stream);
        stream.close();

        ByteArrayOutputStream dstStream = new ByteArrayOutputStream();
        doc.save(dstStream, SaveFormat.PDF);

        OutputStream out = Files.newOutputStream(Paths.get(directoryPath + pdfName));
        dstStream.writeTo(out);

        dstStream.close();
        stream.close();
        out.close();

        return pdfName;

    }


    /**
     * Función que se encarga de obtener las coordenadas de una etiqueta dada.
     * @param label Etiqueta de la que se desea obtener las coordenadas. Debe tener el formato [etiqueta].
     * @param filePath Ubicación del archivo.
     * @return Arreglo con los valores de X, Y y Pagina.
     * @throws Exception
     */
    public static float[] getSignaturePosition(String label, String filePath) throws Exception{
        InputStream is = Files.newInputStream(Paths.get(filePath));
        PDDocument document = PDDocument.load(is);
        CharPosition pdfTextStripper = new CharPosition();

        float[] coordenadas = {-1.0F, -1.0F, -1.0F};
        pdfTextStripper.setTag(label);

        loop:
        for(int k=1; k<=document.getNumberOfPages(); k++){
            pdfTextStripper.setStartPage(k);
            pdfTextStripper.setEndPage(k);

            String pageText = pdfTextStripper.getText(document);
            String[] lines = pageText.split("\\r?\\n");

            for (String line : lines) {
                if (line.toUpperCase().contains(label.toUpperCase())) {
                    String[] temp = line.split("\\Q][\\E");

                    for (String s : temp) {
                        if (s.toUpperCase().contains(label.toUpperCase())) {
                            String[] temp2 = s.split(",");
                            coordenadas = new float[]{
                                    Float.valueOf(temp2[0].replace("[", "").replace("]", "")).floatValue() - 20.0F,
                                    document.getPage(k - 1).getBBox().getHeight() - Float.valueOf(temp2[1].replace("[", "").replace("]", "")).floatValue() - 6.0F,
                                    k
                            };
                            break loop;
                        }
                    }
                }
            }
        }
        document.close();
        is.close();
        return coordenadas;

    }


}
