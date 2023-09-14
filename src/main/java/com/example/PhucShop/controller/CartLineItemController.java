package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Cart;
import com.example.PhucShop.model.CartLineItem;
import com.example.PhucShop.model.Product;
import com.example.PhucShop.model.VariantProduct;
import com.example.PhucShop.repository.CartLineItemRepository;
import com.example.PhucShop.repository.CartRepository;
import com.example.PhucShop.repository.VariantProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/cartlineitem")
public class CartLineItemController extends BaseRestController{

    @Autowired
    private CartLineItemRepository cartLineItemRepository;
    @Autowired
    private VariantProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("")
    public ResponseEntity<?> addItem(@RequestParam int product_id, @RequestParam int cart_id, @RequestBody Map<String, Object> update){
        try{
            if(ObjectUtils.isEmpty(update) || ObjectUtils.isEmpty(update.get("deleted")) || ObjectUtils.isEmpty(update.get("quantity"))){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            VariantProduct variantProduct = this.productRepository.findById(product_id).orElse(null);
            Cart cart = this.cartRepository.findById(cart_id).orElse(null);
            if(ObjectUtils.isEmpty(variantProduct) || ObjectUtils.isEmpty(cart)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            CartLineItem item = new CartLineItem();
            item.setCart(cart);
            item.setVariantProduct(variantProduct);
            item.setDeleted(Boolean.parseBoolean(update.get("deleted").toString()));
            item.setQuantity(Integer.parseInt(update.get("quantity").toString()));
            this.cartLineItemRepository.save(item);
            return success(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("")
    public ResponseEntity<?> updateItem(@RequestParam int item_id, @RequestBody Map<String, Object> update){
        try{
            if(ObjectUtils.isEmpty(update) || ObjectUtils.isEmpty(update.get("deleted")) || ObjectUtils.isEmpty(update.get("quantity"))){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            CartLineItem item = this.cartLineItemRepository.findById(item_id).orElse(null);
            if(ObjectUtils.isEmpty(item)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            item.setDeleted(Boolean.parseBoolean(update.get("deleted").toString()));
            item.setQuantity(Integer.parseInt(update.get("quantity").toString()));
            this.cartLineItemRepository.save(item);
            return success(item);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
