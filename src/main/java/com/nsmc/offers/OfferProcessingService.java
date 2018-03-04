package com.nsmc.offers;

import com.nsmc.domain.offer.OfferApplicable;
import com.nsmc.domain.offer.CartOffers;
import com.nsmc.domain.cart.ShoppingCartVO;
import com.nsmc.offers.offerstrategy.BogofOfferStrategy;
import com.nsmc.offers.offerstrategy.MultipleQuantityOfProductForAPriceOfferStrategy;
import com.nsmc.offers.offerstrategy.OfferStrategy;
import com.nsmc.offers.offerstrategy.ThreeForThePriceOfTwoOfferStrategy;
import com.nsmc.offers.offeradmin.ProductOfferRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sharman on 04/03/2018.
 */
public class OfferProcessingService {

    private final ProductOfferRepository productOfferRepository;
    private final List<OfferStrategy> offerStrategyList;

    public OfferProcessingService(final ProductOfferRepository productOfferRepository) {
        this.productOfferRepository = productOfferRepository;
        offerStrategyList = Arrays.asList(new OfferStrategy[]
                {new ThreeForThePriceOfTwoOfferStrategy(productOfferRepository),
                 new BogofOfferStrategy(productOfferRepository),
                 new MultipleQuantityOfProductForAPriceOfferStrategy(productOfferRepository)});
    }

    public CartOffers processCart(final ShoppingCartVO shoppingCartVO) {
        CartOffers cartOffers = new CartOffers();
        cartOffers.setShoppingCartVO(shoppingCartVO);

        //effectively the shoppingCart is passed through each offer strategy to be evaluated for eligibility of the offer
        List<OfferApplicable> offersApplicable = offerStrategyList.stream()
                .map(offerStrategy -> offerStrategy.applyAndReturnOfferApplicable(shoppingCartVO))
                .filter(offerApplicable -> offerApplicable.isPresent())
                .map(offerApplicable -> offerApplicable.get())
                .collect(Collectors.toList());

        cartOffers.setOffersApplicable(offersApplicable);

        return cartOffers;
    }
}  
