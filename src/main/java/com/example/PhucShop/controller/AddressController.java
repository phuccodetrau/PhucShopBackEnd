package com.example.PhucShop.controller;

import com.example.PhucShop.constants.ResponseCode;
import com.example.PhucShop.model.Address;
import com.example.PhucShop.model.User;
import com.example.PhucShop.repository.AddressRepository;
import com.example.PhucShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/address")
public class AddressController extends BaseRestController{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/{user_id}")
    public ResponseEntity<?> addAddr(@PathVariable int user_id, @RequestBody Map<String, Object> newAddress){
        try{
            if(ObjectUtils.isEmpty(newAddress) || ObjectUtils.isEmpty(newAddress.get("addr_detail"))){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            User found = this.userRepository.findById(user_id).orElse(null);
            if(ObjectUtils.isEmpty(found)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            Address address = new Address();
            address.setUser(found);
            address.setAddr_detail(newAddress.get("addr_detail").toString());
            this.addressRepository.save(address);
            return success(address);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAddress")
    public ResponseEntity<?> getAddr(@RequestParam int user_id){
        try{
            User found = this.userRepository.findById(user_id).orElse(null);
            if(ObjectUtils.isEmpty(found)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            return success(found.getAddresses());
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/updateAddress")
    public ResponseEntity<?> updateAddr(@RequestParam int addr_id, @RequestBody Map<String, Object> update){
        try{
            if(ObjectUtils.isEmpty(update) || ObjectUtils.isEmpty(update.get("addr_detail"))){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            Address foundAddress = this.addressRepository.findById(addr_id).orElse(null);
            if(ObjectUtils.isEmpty(foundAddress)){
                return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
            }
            foundAddress.setAddr_detail(update.get("addr_detail").toString());
            this.addressRepository.save(foundAddress);
            return success(foundAddress);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/deleteAddress/{addr_id}")
    public ResponseEntity<?> deleteAddr(@PathVariable int addr_id){
        try{
            Address found = this.addressRepository.findById(addr_id).orElse(null);
            if(ObjectUtils.isEmpty(found)){
                return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
            }
            found.setUser(null);
            this.addressRepository.deleteById(addr_id);
            return success(found);
        }catch (Exception e){
            e.printStackTrace();
        }
        return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
    }
}
