package kr.pe.paran.myeyes.model;

public class ProductPrice {

    public int _id;

    public String category;
    public String product;
    public String standard;
    public String unit;
    public int price;

    public ProductPrice() {
    }

    public ProductPrice(String category, String product, String standard, String unit, int price) {
        this.category = category;
        this.product = product;
        this.standard = standard;
        this.unit = unit;
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        builder.append("_ID:" + _id);
        builder.append("category:" + category);
        builder.append("product:" + product);
        builder.append("standard:" + standard);
        builder.append("unit:" + unit);
        builder.append("price:" + price);
        builder.append("]");
        return builder.toString();
    }
}
