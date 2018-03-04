package com.nsmc.domain.offer;

import java.math.BigDecimal;

/**
 * Created by sharman on 04/03/2018.
 */
public class ProductMultiBuyOffer {

    private String productName;
    private Integer quantity;
    private BigDecimal totalPrice;

    public ProductMultiBuyOffer(String productName, Integer quantity, BigDecimal totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
