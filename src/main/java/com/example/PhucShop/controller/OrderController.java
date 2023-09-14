package com.example.PhucShop.controller;

import com.example.PhucShop.DTO.OrderDTOResponse;
import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Cart;
import com.example.PhucShop.model.CartLineItem;
import com.example.PhucShop.model.Order;
import com.example.PhucShop.model.User;
import com.example.PhucShop.repository.CartLineItemRepository;
import com.example.PhucShop.repository.CartRepository;
import com.example.PhucShop.repository.OrderRepository;
import com.example.PhucShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/order")
public class OrderController extends BaseRestController{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartLineItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("")
    public ResponseEntity<?> addOrder(@RequestParam int user_id, @RequestBody Map<String, Object> newOrder){
        try{
            if(ObjectUtils.isEmpty(newOrder) || ObjectUtils.isEmpty(newOrder.get("addr_position"))){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            User user = this.userRepository.findById(user_id).orElse(null);
            if(ObjectUtils.isEmpty(user)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            Order order = new Order();
            order.setAddr_position(Integer.parseInt(newOrder.get("addr_position").toString()));
            order.setUser(user);
            order.setTotalAmount(user.getCart().getTotalPrice());
            Date order_time = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(order_time);
            calendar.add(Calendar.DATE, 7);
            Date delivery_time = calendar.getTime();
            order.setOrderTime(order_time);
            order.setDeliveryTime(delivery_time);
//            Cart cart = user.getCart();
//            List<CartLineItem> items = cart.getItems();
//            if(cart == null){
//                return error(123, "cart null");
//            }
//            if(items == null){
//                return error(123, "items null");
//            }
            for(int i = 0; i < order.getUser().getCart().getItems().size(); i++){
                if(order.getUser().getCart().getItems().get(i).isDeleted() == false){
                    order.getUser().getCart().getItems().get(i).setDeleted(true);
                    this.itemRepository.save(order.getUser().getCart().getItems().get(i));
                }
            }
            order.getUser().getCart().setTotalPrice(0);
            this.cartRepository.save(order.getUser().getCart());
            this.orderRepository.save(order);
            return success(new OrderDTOResponse(order));
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
