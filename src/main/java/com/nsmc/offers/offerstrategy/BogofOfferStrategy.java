package com.nsmc.offers.offerstrategy;

import com.nsmc.domain.cart.CartItemVO;
import com.nsmc.domain.offer.OfferApplicable;
import com.nsmc.domain.cart.ShoppingCartVO;
import com.nsmc.offers.offeradmin.ProductOfferRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by sharman on 04/03/2018.
 */
public class BogofOfferStrategy implements OfferStrategy {

    private ProductOfferRepository productOfferRepository;

    public BogofOfferStrategy(ProductOfferRepository productOfferRepository) {
        this.productOfferRepository = productOfferRepository;
    }

    @Override
    public String getSupportedOfferName() {
        return "BOGOF";
    }

    @Override
	public Optional<OfferApplicable> applyAndReturnOfferApplicable(ShoppingCartVO shoppingCartVO) {

		//steps: find all applicable products, and apply

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
        BigDecimal numberOfTimesOfferToBeAppliedForThisProduct = cartItemVO.getQuantity().divideToIntegralValue(BigDecimal.valueOf(2));
        return cartItemVO.getUnitPrice().multiply(numberOfTimesOfferToBeAppliedForThisProduct);
    }
}

