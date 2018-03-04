package com.nsmc.domain.cart;

import java.math.BigDecimal;

/**
 * Created by sharman on 04/03/2018.
 */
public class CartItemVO {
    private String productName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;

    public CartItemVO(String productName, BigDecimal quantity, BigDecimal unitPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal calculateTotalPrice() {
        return quantity.multiply(unitPrice);
    }
} 
