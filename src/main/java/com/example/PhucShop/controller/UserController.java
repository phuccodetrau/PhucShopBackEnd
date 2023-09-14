package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Cart;
import com.example.PhucShop.model.User;
import com.example.PhucShop.repository.RoleRepository;
import com.example.PhucShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController extends BaseRestController{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SessionRegistry sessionRegistry;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<?> addNewUser(@RequestBody Map<String, Object> newUser){
        try{
            if(ObjectUtils.isEmpty(newUser) || ObjectUtils.isEmpty(newUser.get("email")) || ObjectUtils.isEmpty(newUser.get("name")) || ObjectUtils.isEmpty(newUser.get("password")) || ObjectUtils.isEmpty(newUser.get("phone"))){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            User foundUser = this.userRepository.findByName(newUser.get("name").toString()).orElse(null);
            if (!ObjectUtils.isEmpty(foundUser)) {
                return super.error(ResponseCode.DATA_ALREADY_EXISTS.getCode(),
                        ResponseCode.DATA_ALREADY_EXISTS.getMessage());
            }
            User insertedUser = new User();
            insertedUser.setName(newUser.get("name").toString());
            insertedUser.setEmail(newUser.get("email").toString());
            insertedUser.setPhone(newUser.get("phone").toString());
            insertedUser.setPassword(this.passwordEncoder.encode(newUser.get("password").toString()));
            insertedUser.setRoles(this.roleRepository.findByName("ADMIN"));
            Cart cart = new Cart();
            cart.setName(newUser.get("name").toString()+"cart");
            insertedUser.setCart(cart);
            this.userRepository.save(insertedUser);
            if (!ObjectUtils.isEmpty(insertedUser)) {
                return super.success(insertedUser);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUserLoggingIn")
    public ResponseEntity<?> getAllUserLoggingIn(){
        try{
            List<User> users = this.userRepository.findAll();
            return success(users);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserByMap(@PathVariable int id,
                                             @RequestBody(required = false) Map<String, Object> newUser) {
        try {
            if (ObjectUtils.isEmpty(newUser)) {
                return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }

            User foundUser = this.userRepository.findById(id).orElse(null);
            if (ObjectUtils.isEmpty(foundUser)) {
                return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
            }
            foundUser.setEmail(newUser.get("email").toString());
            foundUser.setPhone(newUser.get("phone").toString());
            foundUser.setName(newUser.get("name").toString());
            foundUser.setPassword(this.passwordEncoder.encode(newUser.get("password").toString()));
            this.userRepository.save(foundUser);
            return super.success(foundUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
