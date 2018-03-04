package com.nsmc.offers.offerstrategy;

import com.nsmc.domain.cart.CartItemVO;
import com.nsmc.domain.cart.ShoppingCartVO;
import com.nsmc.domain.offer.OfferApplicable;
import com.nsmc.domain.offer.ProductMultiBuyOffer;
import com.nsmc.offers.offeradmin.ProductOfferRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by sharman on 04/03/2018.
 */
public class MultipleQuantityOfProductForAPriceOfferStrategy implements OfferStrategy {

    private ProductOfferRepository productOfferRepository;

    public MultipleQuantityOfProductForAPriceOfferStrategy(ProductOfferRepository productOfferRepository) {
        this.productOfferRepository = productOfferRepository;
    }

    @Override
    public String getSupportedOfferName() {
        return "Multi_Buy";
    }

    @Override
	public Optional<OfferApplicable> applyAndReturnOfferApplicable(ShoppingCartVO shoppingCartVO) {

		//steps: find all applicable products, and apply

        BigDecimal totalDiscount = shoppingCartVO.getCartItemVOS().stream()
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

        ProductMultiBuyOffer multiBuyOfferForProduct = productOfferRepository.getMultiBuyOfferForProduct(cartItemVO.getProductName());

        if(multiBuyOfferForProduct == null) {
            return BigDecimal.ZERO;
        }

        //calculate multi buy discount

        BigDecimal numberOfTimesOfferToBeAppliedForThisProduct = cartItemVO.getQuantity().divideToIntegralValue(BigDecimal.valueOf(multiBuyOfferForProduct.getQuantity()));
        return cartItemVO.getUnitPrice().multiply(cartItemVO.getQuantity())
                .subtract(numberOfTimesOfferToBeAppliedForThisProduct.multiply(multiBuyOfferForProduct.getTotalPrice()));
    }
}

