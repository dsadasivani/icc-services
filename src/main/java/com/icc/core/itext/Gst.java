package com.icc.core.itext;

public class Gst {
    private double igst;
    private double csGst;
    private double offline;

    public Gst(double igst, double csGst, double offline) {
        super();
        this.igst = igst;
        this.csGst = csGst;
        this.offline = offline;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getCsGst() {
        return csGst;
    }

    public void setCsGst(double csGst) {
        this.csGst = csGst;
    }

    public double getOffline() {
        return offline;
    }

    public void setOffline(double offline) {
        this.offline = offline;
    }
}
