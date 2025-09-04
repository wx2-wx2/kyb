package com.wx2.user.controller;

import com.wx2.common.model.query.IdQuery;
import com.wx2.user.model.query.UserAddressQuery;
import com.wx2.user.model.vo.UserAddressVO;
import com.wx2.user.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    /**
     * 查询用户所有地址
     */
    @GetMapping("/get")
    public List<UserAddressVO> getUserAddress() {
        return userAddressService.getUserAddress();
    }

    /**
     * 根据地址id查询用户地址
     */
    @GetMapping("/{id}")
    public UserAddressVO getUserAddressById(@PathVariable("id") Long addressId) {
        return userAddressService.getUserAddressById(addressId);
    }

    /**
     * 新增用户地址
     */
    @PostMapping("/add")
    public String addUserAddress(@Valid @RequestBody UserAddressQuery query) {
        userAddressService.addUserAddress(query);
        return "新增用户地址成功";
    }

    /**
     * 修改用户地址
     */
    @PutMapping("/update")
    public String updateUserAddress(@Valid @RequestBody UserAddressQuery query) {
        userAddressService.updateUserAddress(query);
        return "修改用户地址成功";
    }

    /**
     * 删除用户地址
     */
    @DeleteMapping("/delete")
    public String deleteUserAddress(@Valid @RequestBody IdQuery query) {
        userAddressService.deleteUserAddress(query.getId());
        return "删除地址成功";
    }

    /**
     * 设置用户默认地址
     */
    @PutMapping("/default")
    public String setDefaultUserAddress(@Valid @RequestBody IdQuery query) {
        userAddressService.setDefaultUserAddress(query.getId());
        return "设置默认地址成功";
    }
}
