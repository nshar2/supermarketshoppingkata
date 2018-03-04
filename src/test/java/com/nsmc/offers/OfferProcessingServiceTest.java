package com.nsmc.offers;

import com.nsmc.domain.offer.CartOffers;
import com.nsmc.domain.cart.CartItemVO;
import com.nsmc.domain.cart.ShoppingCartVO;
import com.nsmc.domain.offer.ProductMultiBuyOffer;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;

/**
 * Created by sharman on 04/03/2018.
 * This is an Integration test which tests the offer system as a single unit
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class OfferProcessingServiceTest {

    private OfferProcessingService underTest;
    private ShoppingCartVO shoppingCartVO;

    @Mock
    private ProductOfferRepository productOfferRepository;

    @Before
    public void setUp() throws Exception {
        underTest = new OfferProcessingService(productOfferRepository);

        List<CartItemVO> cartItemVOS = new ArrayList<>();
        cartItemVOS.add(new CartItemVO("APPLE", BigDecimal.valueOf(3), BigDecimal.valueOf(1.69)));
        cartItemVOS.add(new CartItemVO("GRAPES", BigDecimal.valueOf(4), BigDecimal.valueOf(2.50)));
        cartItemVOS.add(new CartItemVO("COKE", BigDecimal.valueOf(4), BigDecimal.valueOf(0.70)));

        shoppingCartVO = new ShoppingCartVO(cartItemVOS);
    }

    @Test
    public void shouldNotProcessCartForAnyOffers_noEligibleOffers() throws Exception {

        //We have not set any offers on products

        CartOffers cartOffers = underTest.processCart(shoppingCartVO);

        assertEquals(BigDecimal.ZERO, cartOffers.calculateTotalOfferDiscounts());
    }

    @Test
    public void shouldProcessCartForBOGOF() throws Exception {

        //offer setup: BOGOF on APPLE/ORANGE/KELLOGS
        String testOfferName = "BOGOF";
        String[] products = {"APPLE", "ORANGE", "KELLOGS"};
        List productsForOfferName = Arrays.asList(products);

        Mockito.when(productOfferRepository.getProductsForOfferName(eq(testOfferName))).thenReturn(productsForOfferName);

        CartOffers cartOffers = underTest.processCart(shoppingCartVO);

        assertEquals(BigDecimal.valueOf(17.87), cartOffers.calculatePriceBeforeDiscounts());
        assertEquals(BigDecimal.valueOf(1.69), cartOffers.calculateTotalOfferDiscounts());
        assertEquals(BigDecimal.valueOf(16.18), cartOffers.calculateOrderTotal());
        assertDiscountIncludesOfferType(cartOffers, "BOGOF");

        Mockito.verify(productOfferRepository).getProductsForOfferName(eq(testOfferName));
    }

    @Test
    public void shouldProcessCartForTFPOT() throws Exception {

        //offer setup: THREE_FOR_PRICE_OF_TWO on APPLE
        String testOfferName = "THREE_FOR_PRICE_OF_TWO";
        String[] products = {"APPLE"};
        List productsForOfferName = Arrays.asList(products);

        Mockito.when(productOfferRepository.getProductsForOfferName(eq(testOfferName))).thenReturn(productsForOfferName);

        CartOffers cartOffers = underTest.processCart(shoppingCartVO);

        assertEquals(BigDecimal.valueOf(1.69), cartOffers.calculateTotalOfferDiscounts());
        assertDiscountIncludesOfferType(cartOffers, "THREE_FOR_PRICE_OF_TWO");

        Mockito.verify(productOfferRepository).getProductsForOfferName(eq(testOfferName));
    }

    @Test
    public void shouldProcessCartForMultiBuyOffers() throws Exception {

        //offer setup: Multi_Buy on COKE
        String testOfferName = "Multi_Buy";

        String product = "COKE";

        ProductMultiBuyOffer productMultiBuyOffer = new ProductMultiBuyOffer(product, 2, BigDecimal.valueOf(1.00));

        Mockito.when(productOfferRepository.getMultiBuyOfferForProduct(eq(product))).thenReturn(productMultiBuyOffer);

        CartOffers cartOffers = underTest.processCart(shoppingCartVO);

        assertEquals(BigDecimal.valueOf(0.80), cartOffers.calculateTotalOfferDiscounts());
        assertDiscountIncludesOfferType(cartOffers, "Multi_Buy");

        Mockito.verify(productOfferRepository).getMultiBuyOfferForProduct(eq(product));
    }

    @Test
    public void shouldProcessCartForMultipleOffers() throws Exception {

        //offer setup:: THREE_FOR_PRICE_OF_TWO on APPLE
        String tfpotOfferName = "THREE_FOR_PRICE_OF_TWO";
        String[] products = {"APPLE"};
        List productsForOfferNameTFPOT = Arrays.asList(products);

        Mockito.when(productOfferRepository.getProductsForOfferName(eq(tfpotOfferName))).thenReturn(productsForOfferNameTFPOT);

        //offer setup: setting BOGOF on GRAPES
        String bogofOfferName = "BOGOF";
        String[] productsForOfferNameBOGOF = {"GRAPES"};
        List productsForOfferNameBOGOFList = Arrays.asList(productsForOfferNameBOGOF);

        Mockito.when(productOfferRepository.getProductsForOfferName(eq(bogofOfferName))).thenReturn(productsForOfferNameBOGOFList);

        CartOffers cartOffers = underTest.processCart(shoppingCartVO);

        assertEquals(BigDecimal.valueOf(6.69), cartOffers.calculateTotalOfferDiscounts());
        assertDiscountIncludesOfferType(cartOffers, "THREE_FOR_PRICE_OF_TWO");
        assertDiscountIncludesOfferType(cartOffers, "BOGOF");
    }

    private void assertDiscountIncludesOfferType(CartOffers cartOffers, String offerName) {
        boolean offerIncluded = cartOffers.getOffersApplicable().stream()
                .anyMatch(offerApplicable -> offerApplicable.getOfferName().equals(offerName));
        assertTrue(offerIncluded);
    }
}