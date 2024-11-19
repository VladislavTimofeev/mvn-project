package com.vlad.mapper;

import com.vlad.dto.request.RequestCreateEditDto;
import com.vlad.entity.Request;
import com.vlad.entity.User;
import com.vlad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestCreateEditMapper implements Mapper<RequestCreateEditDto, Request> {

    private final UserRepository userRepository;

    @Override
    public Request map(RequestCreateEditDto object) {
        Request request = new Request();
        copy(object, request);
        return request;
    }

    @Override
    public Request map(RequestCreateEditDto fromObject, Request toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(RequestCreateEditDto object, Request request) {
        request.setCustomer(getUser(object.getCustomerId()));
        request.setStatus(object.getStatus());
        request.setCargoDetails(object.getCargoDetails());
        request.setWeight(object.getWeight());
        request.setPalletCount(object.getPalletCount());
        request.setRefrigerated(object.getRefrigerated());
        request.setPickupAddress(object.getPickupAddress());
        request.setDeliveryAddress(object.getDeliveryAddress());
        request.setCreationDate(object.getCreationDate());
        request.setCarrier(getUser(object.getCarrierId()));
    }

    private User getUser(Long id) {
        return Optional.ofNullable(id)
                .flatMap(userRepository::findById)
                .orElse(null);
    }
}
