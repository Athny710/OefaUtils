package org.oefa.gob.pe.Oefa.util;

import java.io.InputStream;

public class LoaderUtil {

    public static InputStream loadResource(String resourceName){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(resourceName);

    }
}
