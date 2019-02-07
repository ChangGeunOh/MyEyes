package kr.pe.paran.myeyes.model;

public class UnitPrice {

    public int _id      = -1;

    public String category;
    public String product;
    public String standard;
    public String unit;
    public int price;

    public UnitPrice() {
    }

    public UnitPrice(String category, String product, String standard, String unit, int price) {
        this.category = category;
        this.product = product;
        this.standard = standard;
        this.unit = unit;
        this.price = price;
    }

    @Override
    public String toString() {
        return "UnitPrice{" +
                "_id=" + _id +
                ", category='" + category + '\'' +
                ", product='" + product + '\'' +
                ", standard='" + standard + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                '}';
    }
}
