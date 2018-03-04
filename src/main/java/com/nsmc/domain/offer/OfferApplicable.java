package com.nsmc.domain.offer;

import java.math.BigDecimal;

/**
 * Created by sharman on 04/03/2018.
 */
public class OfferApplicable {
    private String offerName;
    private BigDecimal discountAmount;

    public OfferApplicable(String offerName, BigDecimal discountAmount) {
        this.offerName = offerName;
        this.discountAmount = discountAmount;
    }

    public String getOfferName() {
        return offerName;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
} 
