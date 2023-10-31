package org.oefa.gob.pe.Oefa.domain;

public class GlosaOefa {


    private String text;
    private String url;
    private String barcodePin;


    public GlosaOefa(String text, String url, String barcodePin) {
        this.text = text;
        this.url = url;
        this.barcodePin = barcodePin;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBarcodePin() {
        return barcodePin;
    }

    public void setBarcodePin(String barcodePin) {
        this.barcodePin = barcodePin;
    }
}
