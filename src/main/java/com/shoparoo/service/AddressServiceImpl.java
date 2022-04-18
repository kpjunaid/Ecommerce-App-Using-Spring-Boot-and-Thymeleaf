package com.shoparoo.service;

import com.shoparoo.dto.AddressDto;
import com.shoparoo.dto.UserUpdateDto;
import com.shoparoo.entity.Address;
import com.shoparoo.exception.AddressNotFoundException;
import com.shoparoo.mapper.MapStructMapper;
import com.shoparoo.mapper.MapStructMapperUpdate;
import com.shoparoo.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final MapStructMapper mapStructMapper;
    private final MapStructMapperUpdate mapStructMapperUpdate;

    public AddressServiceImpl(AddressRepository addressRepository,
                              MapStructMapper mapStructMapper,
                              MapStructMapperUpdate mapStructMapperUpdate) {
        this.addressRepository = addressRepository;
        this.mapStructMapper = mapStructMapper;
        this.mapStructMapperUpdate = mapStructMapperUpdate;
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new AddressNotFoundException("Address not found"));
    }

    @Override
    public Address saveNewAddress(AddressDto addressDto) {
        return addressRepository.save(mapStructMapper.addressDtoToAddress(addressDto));
    }

    @Override
    public Address updateAddressInfo(Long id, UserUpdateDto userUpdateDto) {
        Address address = getAddressById(id);
        AddressDto addressDto = mapStructMapper.userUpdateDtoToAddressDto(userUpdateDto);
        mapStructMapperUpdate.updateAddressFromAddressDto(addressDto, address);

        return addressRepository.save(address);
    }
}
