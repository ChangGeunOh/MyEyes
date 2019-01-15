package kr.pe.paran.myeyes.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Estimate implements Serializable {

    public String custmer;
    public String reg_date;
    public ArrayList<ProductPrice>  productPrices;

    public Estimate() {
        productPrices = new ArrayList<>();
    }

    public Estimate(String custmer) {
        this.custmer = custmer;
        productPrices = new ArrayList<>();
    }


    public void addProduct(ProductPrice productPrice) {
        productPrices.add(productPrice);
    }
}
