package org.oefa.gob.pe.Oefa.domain;

public class GlosaPeru {

    private String topLine;
    private String bottomLine;
    private String url;

    public GlosaPeru(String topLine, String bottomLine, String url) {
        this.topLine = topLine;
        this.bottomLine = bottomLine;
        this.url = url;
    }

    public String getTopLine() {
        return topLine;
    }

    public void setTopLine(String topLine) {
        this.topLine = topLine;
    }

    public String getBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(String bottomLine) {
        this.bottomLine = bottomLine;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
