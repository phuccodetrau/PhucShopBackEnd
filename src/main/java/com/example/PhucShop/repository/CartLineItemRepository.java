package com.example.PhucShop.repository;

import com.example.PhucShop.model.CartLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartLineItemRepository extends JpaRepository<CartLineItem, Integer> {
}
