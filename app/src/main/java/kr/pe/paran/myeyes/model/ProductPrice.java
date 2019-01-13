package kr.pe.paran.myeyes.model;

public class ProductPrice extends UnitPrice {

    public int      count   = 1;
    public long     sum     = 0;
//    public String   reg_date;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("[");
        builder.append("_ID:" + _id + ",");
        builder.append("category:" + category + ",");
        builder.append("product:" + product + ",");
        builder.append("standard:" + standard + ",");
        builder.append("unit:" + unit + ",");
        builder.append("price:" + price + ",");
        builder.append("count:" + count + ",");
        builder.append("sum:" + sum + "]");

        return builder.toString();
    }
}
