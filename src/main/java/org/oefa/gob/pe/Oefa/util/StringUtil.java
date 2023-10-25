package org.oefa.gob.pe.Oefa.util;

public class StringUtil {

    public static String extractFileName(String fileName){
        int pos = fileName.lastIndexOf('.');

        return fileName.substring(0, pos);

    }
}
