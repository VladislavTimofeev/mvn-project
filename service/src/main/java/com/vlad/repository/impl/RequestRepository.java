package com.vlad.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.entity.QRequest;
import com.vlad.entity.QUser;
import com.vlad.entity.Request;
import com.vlad.repository.AbstractRepository;
import com.vlad.repository.QPredicate;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestRepository extends AbstractRepository<Long, Request> {

    public RequestRepository(EntityManager entityManager) {
        super(Request.class, entityManager);
    }

    public List<Request> getRequestByFilter(RequestFilterDto filterDto) {
        Predicate predicate = QPredicate.builder()
                .add(filterDto.getStatus(), QRequest.request.status::eq)
                .add(filterDto.getPickupAddress(), QRequest.request.pickupAddress::eq)
                .add(filterDto.getDeliveryAddress(), QRequest.request.deliveryAddress::eq)
                .add(filterDto.getCreationDate(), QRequest.request.creationDate::eq)
                .buildAnd();
        return new JPAQuery<>(entityManager)
                .select(QRequest.request)
                .from(QRequest.request)
                .join(QRequest.request.carrier, QUser.user)
                .where(predicate)
                .fetch();
    }
}
