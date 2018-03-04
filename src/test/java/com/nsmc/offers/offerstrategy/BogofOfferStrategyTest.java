package com.nsmc.offers.offerstrategy;

import com.nsmc.domain.cart.CartItemVO;
import com.nsmc.domain.cart.ShoppingCartVO;
import com.nsmc.domain.offer.CartOffers;
import com.nsmc.domain.offer.OfferApplicable;
import com.nsmc.offers.offeradmin.ProductOfferRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;

/**
 * Created by sharman on 04/03/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class BogofOfferStrategyTest {

    private ShoppingCartVO shoppingCartVO;
    private BogofOfferStrategy underTest;

    @Mock
    private ProductOfferRepository productOfferRepository;

    @Before
    public void setUp() {
        List<CartItemVO> cartItemVOS = new ArrayList<>();
        cartItemVOS.add(new CartItemVO("APPLE", BigDecimal.valueOf(3), BigDecimal.valueOf(1.69)));
        cartItemVOS.add(new CartItemVO("GRAPES", BigDecimal.valueOf(4), BigDecimal.valueOf(2.50)));
        cartItemVOS.add(new CartItemVO("COKE", BigDecimal.valueOf(4), BigDecimal.valueOf(0.70)));

        shoppingCartVO = new ShoppingCartVO(cartItemVOS);

        underTest = new BogofOfferStrategy(productOfferRepository);
    }

    @Test
    public void shouldApplyAndReturnOfferApplicable_BOGOF() {

        //offer setup: BOGOF on APPLE/ORANGE/KELLOGS

        String offerName = "BOGOF";
        String[] products = {"APPLE", "ORANGE", "KELLOGS"};
        List productsForOfferName = Arrays.asList(products);

        Mockito.when(productOfferRepository.getProductsForOfferName(eq(offerName))).thenReturn(productsForOfferName);

        Optional<OfferApplicable> offerApplicable = underTest.applyAndReturnOfferApplicable(shoppingCartVO);

        assertTrue(offerApplicable.isPresent());
        assertEquals(BigDecimal.valueOf(1.69), offerApplicable.get().getDiscountAmount());
        assertEquals(offerName, offerApplicable.get().getOfferName());
        Mockito.verify(productOfferRepository).getProductsForOfferName(eq(offerName));
    }

}