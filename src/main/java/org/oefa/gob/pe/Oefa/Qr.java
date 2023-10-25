package org.oefa.gob.pe.Oefa;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Image;
import org.oefa.gob.pe.Oefa.domain.Dimension;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Qr {


    private static final String IMG_EXTENSION = "png";


    /**
     * Función que se encarga de generar imagen QR utilizando la URL brindada.
     * @param dimension Objeto de dimensiones que contiene la posisión y tamaño del QR que se va a generar.
     * @param url URL al que redireccionará al usuario al momento de escanear la imagen.
     * @return Imagen
     * @throws Exception
     */
    public static Image generateQr(Dimension dimension, String url) throws Exception{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, dimension.getWidth(), dimension.getHeight());
        BufferedImage buffImage = new BufferedImage(dimension.getWidth(), dimension.getHeight(), 1);

        for (int yi = 0; yi < dimension.getHeight(); ++yi) {
            for (int xi = 0; xi < dimension.getWidth(); ++xi) {
                int grayValue = (matrix.get(xi, yi) ? 0 : 1) & 0xFF;
                buffImage.setRGB(xi, yi, grayValue == 0 ? 0 : 0xFFFFFF);
            }
        }

        BufferedImage buffTmpImage = new BufferedImage(buffImage.getWidth(null), buffImage.getHeight(null), 6);
        buffTmpImage.getGraphics().drawImage(buffImage, 0, 0, null);

        BufferedImage buffOutImage = buffTmpImage.getSubimage(0, 0, dimension.getWidth(), dimension.getHeight());
        ByteArrayOutputStream boutImage = new ByteArrayOutputStream();
        ImageIO.write(buffOutImage, IMG_EXTENSION, boutImage);

        Image img= Image.getInstance(boutImage.toByteArray());
        img.setAbsolutePosition(dimension.getX(), dimension.getY());
        img.scaleAbsolute(dimension.getScale(), dimension.getScale());

        boutImage.close();

        return img;

    }


}
