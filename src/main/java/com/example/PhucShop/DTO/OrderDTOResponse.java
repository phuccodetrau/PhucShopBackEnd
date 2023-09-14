package com.example.PhucShop.DTO;

import com.example.PhucShop.model.Order;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDTOResponse {
    private Date orderDate;
    private Date deliveryDate;
    private double totalAmount;
    private String address;
    private String username;

    public OrderDTOResponse(Order order) {
        this.orderDate = order.getOrderTime();
        this.deliveryDate = order.getDeliveryTime();
        this.address = order.getUser().getAddresses().get(order.getAddr_position()).getAddr_detail();
        this.totalAmount = order.getTotalAmount();
        this.username = order.getUser().getName();
    }
}
