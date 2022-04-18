package com.shoparoo.service;

import com.shoparoo.dto.AddressDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.Address;

public interface AddressService {
    Address getAddressById(Long id);
    Address saveNewAddress(AddressDto addressDto);
    Address updateAddressInfo(Long id, UserUpdateDto userUpdateDto);
}
