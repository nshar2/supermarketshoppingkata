package com.nsmc.offers.offeradmin;

import com.nsmc.domain.offer.ProductMultiBuyOffer;

import java.util.List;

/**
 * Created by sharman on 04/03/2018.
 *
 * The objective of this repository is to to maintain the offers on products
 */
public interface ProductOfferRepository {

    /**
     * returns the products which are eligible for a given offer type
     */
    List<String> getProductsForOfferName(String offerName);

    ProductMultiBuyOffer getMultiBuyOfferForProduct(String productName);
}
