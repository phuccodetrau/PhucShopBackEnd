package com.example.PhucShop.controller;

import com.example.PhucShop.DTO.AuthenDTORequest;
import com.example.PhucShop.DTO.AuthenDTOResponse;
import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
public class AuthenController extends BaseRestController{
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthenDTORequest authen) {
        try {
            this.authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authen.getName(), authen.getPassword()));

            String token = JwtUtils.generateToken(authen.getName());
            AuthenDTOResponse response = new AuthenDTOResponse(token, "Dang nhap thanh cong!");
            return success(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
