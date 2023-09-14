package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Cart;
import com.example.PhucShop.model.CartLineItem;
import com.example.PhucShop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cart")
public class CartController extends BaseRestController{
    @Autowired
    private CartRepository cartRepository;


    @GetMapping("/totalprice")
    public ResponseEntity<?> getPrice(@RequestParam int cart_id){
        try{
            Cart cart = this.cartRepository.findById(cart_id).orElse(null);
            if(ObjectUtils.isEmpty(cart)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            double totalPrice = 0;
            for(CartLineItem item : cart.getItems()){
                if (item.isDeleted() == false){
                    totalPrice += item.getVariantProduct().getPrice() * item.getQuantity();
                }
            }
            cart.setTotalPrice(totalPrice);
            this.cartRepository.save(cart);
            return success(totalPrice);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
