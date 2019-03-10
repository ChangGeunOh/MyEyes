package kr.pe.paran.myeyes.model;

import java.io.Serializable;

public class OptionItem implements Serializable {

    public String category;
    public String product = "CCTV";
    public String summary;
    public int    price;

    public OptionItem(String category, String product, String summary, int price) {
        this.category = category;
        this.product = product;
        this.summary = summary;
        this.price = price;
    }

    @Override
    public String toString() {
        return "OptionItem{" +
                "category='" + category + '\'' +
                ", product='" + product + '\'' +
                ", summary='" + summary + '\'' +
                ", price=" + price +
                '}';
    }
}
