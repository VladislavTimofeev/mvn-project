package com.vlad.repository;

import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.entity.Request;

import java.util.List;

public interface FilterRequestRepository {

    List<Request> findAllByFilter(RequestFilterDto requestFilterDto);
}
