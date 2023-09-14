package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Category;
import com.example.PhucShop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/category")
public class CategoryController extends BaseRestController{
    @Autowired
    private CategoryRepository repository;

    @PostMapping("")
    public ResponseEntity<?> createCategory(@RequestBody Map<String, Object> newCategory){
        try{
            if(ObjectUtils.isEmpty(newCategory) || ObjectUtils.isEmpty(newCategory.get("name"))){
                return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
            }
            Category category = new Category();
            category.setName(newCategory.get("name").toString());
            this.repository.save(category);
            return success(category);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @GetMapping("/getAllCategory")
    public ResponseEntity<?> getAllCategory(){
        try {
            List<Category> categories = this.repository.findAll();
            return success(categories);
        }catch (Exception e){

        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
