package com.nsmc.domain.offer;

import java.util.Collections;
import java.util.List;

/**
 * Created by sharman on 04/03/2018.
 */
public class ProductOffers {
    private String productName;
    private List<String> offerNameList;

    public ProductOffers(String productName, List<String> offerNameList) {
        this.productName = productName;
        this.offerNameList = offerNameList;
    }

    public String getProductName() {
        return productName;
    }

    public List<String> getOfferNameList() {
        return Collections.unmodifiableList(offerNameList);
    }
} 
