package com.nsmc.offers.offerstrategy;

import com.nsmc.domain.offer.OfferApplicable;
import com.nsmc.domain.cart.ShoppingCartVO;

import java.util.Optional;

/**
 * Created by sharman on 04/03/2018.
 */
public interface OfferStrategy {

    String getSupportedOfferName();
	Optional<OfferApplicable> applyAndReturnOfferApplicable(ShoppingCartVO shoppingCartVO);
}
