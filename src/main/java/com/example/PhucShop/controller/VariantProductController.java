package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Category;
import com.example.PhucShop.model.Product;
import com.example.PhucShop.model.VariantProduct;
import com.example.PhucShop.repository.ProductRepository;
import com.example.PhucShop.repository.VariantProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/variantproduct")
public class VariantProductController extends BaseRestController{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantProductRepository variantProductRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> createVariantProduct(@RequestBody Map<String, Object> update){
        try{
            if(ObjectUtils.isEmpty(update) || ObjectUtils.isEmpty(update.get("name")) || ObjectUtils.isEmpty(update.get("product_id")) || ObjectUtils.isEmpty(update.get("model_year")) || ObjectUtils.isEmpty(update.get("price"))){
                return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
            }
            int product_id = Integer.parseInt(update.get("product_id").toString());
            Product product = this.productRepository.findById(product_id).orElse(null);
            if(ObjectUtils.isEmpty(product)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            VariantProduct variantProduct = new VariantProduct();
            variantProduct.setProduct(product);
            variantProduct.setName(update.get("name").toString());
            variantProduct.setPrice(Double.parseDouble(update.get("price").toString()));
            variantProduct.setModelYear(update.get("model_year").toString());
            this.variantProductRepository.save(variantProduct);
            return success(variantProduct);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @GetMapping("/getProductByProduct")
    public ResponseEntity<?> getVariantProductByProduct(@RequestParam int productId){
        Product product = this.productRepository.findById(productId).orElse(null);
        if(ObjectUtils.isEmpty(product)){
            return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
        }
        return success(product.getVariantProducts());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("")
    public ResponseEntity<?> updateVariantProduct(@RequestParam int id, @RequestBody Map<String, Object> update){
        try{
            if(ObjectUtils.isEmpty(update) || ObjectUtils.isEmpty(update.get("name")) || ObjectUtils.isEmpty(update.get("model_year")) || ObjectUtils.isEmpty(update.get("price"))) {
                return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
            }
            VariantProduct variantProduct = this.variantProductRepository.findById(id).orElse(null);
            if(ObjectUtils.isEmpty(variantProduct)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            variantProduct.setName(update.get("name").toString());
            variantProduct.setPrice(Double.parseDouble(update.get("price").toString()));
            variantProduct.setModelYear(update.get("model_year").toString());
            this.variantProductRepository.save(variantProduct);
            return success(variantProduct);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
