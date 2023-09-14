package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Category;
import com.example.PhucShop.model.Product;
import com.example.PhucShop.repository.CategoryRepository;
import com.example.PhucShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/product")
public class ProductController extends BaseRestController{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createProduct(@RequestBody Map<String, Object> updateProduct){
        try{
            if(ObjectUtils.isEmpty(updateProduct) || ObjectUtils.isEmpty(updateProduct.get("name")) || ObjectUtils.isEmpty(updateProduct.get("category_id"))){
                return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
            }
            int category_id = Integer.parseInt(updateProduct.get("category_id").toString());
            Category category = this.categoryRepository.findById(category_id).orElse(null);
            if(ObjectUtils.isEmpty(category)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            Product product = new Product();
            product.setCategory(category);
            product.setName(updateProduct.get("name").toString());
            this.productRepository.save(product);
            return success(product);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("")
    public ResponseEntity<?> updateProduct(@RequestParam int id, @RequestBody Map<String, Object> updateProduct){
        try{
            if(ObjectUtils.isEmpty(updateProduct) || ObjectUtils.isEmpty(updateProduct.get("name"))){
                return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
            }
            Product product = this.productRepository.findById(id).orElse(null);
            if(ObjectUtils.isEmpty(product)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            product.setName(updateProduct.get("name").toString());
            this.productRepository.save(product);
            return success(product);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @GetMapping("/getProductByCategory")
    public ResponseEntity<?> getProductByCategory(@RequestParam int categoryId, Pageable pageable){
        Category category = this.categoryRepository.findById(categoryId).orElse(null);
        if(ObjectUtils.isEmpty(category)) {
            return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
        }
        List<Product> products = this.productRepository.findByCategoryId(categoryId, pageable);
        return success(products);
    }

    @GetMapping("/getProductById")
    public ResponseEntity<?> getProductById(@RequestParam int productId){
        Product product = this.productRepository.findById(productId).orElse(null);
        if(ObjectUtils.isEmpty(product)){
            return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
        }
        return success(product);
    }
}
