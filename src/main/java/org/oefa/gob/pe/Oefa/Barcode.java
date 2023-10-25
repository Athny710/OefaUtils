package org.oefa.gob.pe.Oefa;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import org.oefa.gob.pe.Oefa.domain.Dimension;

public class Barcode {


    /**
     * Función que se encarga de generar la imagen del código del barras.
     * @param pcb Contenido del archivo donde se insertará la imagen.
     * @param dimension Objeto de dimensiones que contiene la posición y la escala.
     * @param code El código a partir del cual se generará el código de barra.
     * @return Imagen del código de barra.
     */
    public static Image generateBarcode(PdfContentByte pcb, Dimension dimension, String code){
        Barcode128 code128 = new Barcode128();
        code128.setCode(code.trim());
        code128.setCodeType(9);

        Image barcodeImage = code128.createImageWithBarcode(pcb, null, null);
        barcodeImage.setAbsolutePosition(dimension.getX(), dimension.getY());
        barcodeImage.scalePercent(dimension.getScale());

        return barcodeImage;

    }

}
