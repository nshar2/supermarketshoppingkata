package com.nsmc.domain.cart;

import java.util.Collections;
import java.util.List;

/**
 * Created by sharman on 04/03/2018.
 */
public class ShoppingCartVO {
    private List<CartItemVO> cartItemVOS;

    public ShoppingCartVO(List<CartItemVO> cartItemVOS) {
        this.cartItemVOS = cartItemVOS;
    }

    public List<CartItemVO> getCartItemVOS() {
        return Collections.unmodifiableList(cartItemVOS);
    }
} 
