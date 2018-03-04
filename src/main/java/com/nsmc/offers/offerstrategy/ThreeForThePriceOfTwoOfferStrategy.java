package com.nsmc.offers.offerstrategy;

import java.math.BigDecimal;
import java.util.*;

import com.nsmc.domain.cart.CartItemVO;
import com.nsmc.domain.offer.OfferApplicable;
import com.nsmc.domain.cart.ShoppingCartVO;
import com.nsmc.offers.offeradmin.ProductOfferRepository;

/**
 * Created by sharman on 04/03/2018.
 */
public class ThreeForThePriceOfTwoOfferStrategy  implements OfferStrategy {

    private ProductOfferRepository productOfferRepository;

    public ThreeForThePriceOfTwoOfferStrategy(ProductOfferRepository productOfferRepository) {
        this.productOfferRepository = productOfferRepository;
    }

    @Override
    public String getSupportedOfferName() {
        return "THREE_FOR_PRICE_OF_TWO";
    }

    @Override
	public Optional<OfferApplicable> applyAndReturnOfferApplicable(ShoppingCartVO shoppingCartVO) {

		//steps: find all products in the cart which are eligible for this offer, and apply offers

        List<String> productsForOfferName = productOfferRepository.getProductsForOfferName(getSupportedOfferName());

        BigDecimal totalDiscount = shoppingCartVO.getCartItemVOS().stream()
                .filter(cartItemVO -> productsForOfferName.contains(cartItemVO.getProductName()))
                .map(this::applyOffer)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDiscount.equals(BigDecimal.ZERO)) {
            return Optional.empty();
        }
        else {
            return Optional.of(new OfferApplicable(getSupportedOfferName(), totalDiscount));
        }
	}

    private BigDecimal applyOffer(CartItemVO cartItemVO) {
        BigDecimal numberOfTimesOfferToBeAppliedForThisItem = cartItemVO.getQuantity().divideToIntegralValue(BigDecimal.valueOf(3));
        return cartItemVO.getUnitPrice().multiply(numberOfTimesOfferToBeAppliedForThisItem);
    }

}

