package kr.pe.paran.myeyes.model;

public class ProductPrice extends UnitPrice {

    public int      count   = 1;
    public int      subCount= 1;
    public long     sum     = 0;
//    public String   reg_date;


    @Override
    public String toString() {
        return "ProductPrice{" +
                "count=" + count +
                ", subCount=" + subCount +
                ", sum=" + sum +
                ", _id=" + _id +
                ", category='" + category + '\'' +
                ", product='" + product + '\'' +
                ", standard='" + standard + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                '}';
    }
}
