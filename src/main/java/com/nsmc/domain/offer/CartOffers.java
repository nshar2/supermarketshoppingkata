package com.nsmc.domain.offer;

import com.nsmc.domain.cart.ShoppingCartVO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by sharman on 04/03/2018.
 */
public class CartOffers {
    private ShoppingCartVO shoppingCartVO;
    private List<OfferApplicable> offersApplicable;

    public CartOffers() {
    }

    public void setShoppingCartVO(ShoppingCartVO shoppingCartVO) {
        this.shoppingCartVO = shoppingCartVO;
    }

    public void setOffersApplicable(List<OfferApplicable> offersApplicable) {
        this.offersApplicable = offersApplicable;
    }

    public List<OfferApplicable> getOffersApplicable() {
        return Collections.unmodifiableList(offersApplicable);
    }

    public BigDecimal calculatePriceBeforeDiscounts() {

        return shoppingCartVO.getCartItemVOS().stream()
                .map(cartItem -> cartItem.calculateTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalOfferDiscounts() {
        return offersApplicable.stream()
                .map(offerApplicable -> offerApplicable.getDiscountAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateOrderTotal() {
        return calculatePriceBeforeDiscounts().subtract(calculateTotalOfferDiscounts());
    }
}
