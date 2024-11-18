package com.vlad.mapper;

import com.vlad.dto.request.RequestReadDto;
import com.vlad.entity.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestReadMapper implements Mapper<Request, RequestReadDto>{

    private final UserReadMapper userReadMapper;

    @Override
    public RequestReadDto map(Request object) {
        return new RequestReadDto(
                object.getId(),
                userReadMapper.map(object.getCustomer()),
                object.getStatus(),
                object.getCargoDetails(),
                object.getWeight(),
                object.getPalletCount(),
                object.getRefrigerated(),
                object.getPickupAddress(),
                object.getDeliveryAddress(),
                object.getCreationDate(),
                userReadMapper.map(object.getCarrier())
        );
    }
}
