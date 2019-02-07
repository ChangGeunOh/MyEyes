package kr.pe.paran.myeyes.model;

public class Customer {

    public String customer;
    public String period;
    public String reg_date;

    public Customer(String customer, String period, String reg_date) {
        this.customer = customer;
        this.period = period;
        this.reg_date = reg_date;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer='" + customer + '\'' +
                ", period='" + period + '\'' +
                ", reg_date='" + reg_date + '\'' +
                '}';
    }
}
