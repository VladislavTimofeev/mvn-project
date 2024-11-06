package com.vlad.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.entity.QRequest;
import com.vlad.entity.QUser;
import com.vlad.entity.Request;
import com.vlad.repository.FilterRequestRepository;
import com.vlad.repository.QPredicate;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FilterRequestRepositoryImpl implements FilterRequestRepository {

    private final EntityManager entityManager;

    @Override
    public List<Request> findAllByFilter(RequestFilterDto requestFilterDto) {
        Predicate predicate = QPredicate.builder()
                .add(requestFilterDto.getStatus(), QRequest.request.status::eq)
                .add(requestFilterDto.getPickupAddress(), QRequest.request.pickupAddress::eq)
                .add(requestFilterDto.getDeliveryAddress(), QRequest.request.deliveryAddress::eq)
                .add(requestFilterDto.getCreationDate(), QRequest.request.creationDate::eq)
                .buildAnd();
        return new JPAQuery<>(entityManager)
                .select(QRequest.request)
                .from(QRequest.request)
                .where(predicate)
                .fetch();
    }
}
