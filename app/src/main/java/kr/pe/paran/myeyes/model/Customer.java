package kr.pe.paran.myeyes.model;

public class Customer {

    public String customer;
    public String reg_date;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("[");
        builder.append("customer:" + customer + ",");
        builder.append("reg_date:" + reg_date + "]");

        return builder.toString();
    }
}
