package com.mobweb.devizapp.model;

public class Currency {
    String code;
    Double rate;
    int img_src;
    String symbol;

    public Currency(String code, int img_src, String symbol) {
        this.code = code;
        this.img_src = img_src;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public int getImg_src() {
        return img_src;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getSymbol() {
        return symbol;
    }

}
