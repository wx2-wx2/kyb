package com.wx2.user.model.query;

import lombok.Data;

@Data
public class UserAddressQuery {
    private Long addressId;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault = 0;
}
