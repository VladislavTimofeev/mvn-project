package com.vlad.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.entity.QUser;
import com.vlad.entity.QVehicle;
import com.vlad.entity.Vehicle;
import com.vlad.repository.FilterVehicleRepository;
import com.vlad.repository.QPredicate;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

@RequiredArgsConstructor
public class FilterVehicleRepositoryImpl implements FilterVehicleRepository {

    private final EntityManager entityManager;

    @Override
    public List<Vehicle> findAllByFilter(VehicleFilterDto vehicleFilterDto) {
        Predicate predicate = QPredicate.builder()
                .add(vehicleFilterDto.getPalletCapacity(), QVehicle.vehicle.palletCapacity::eq)
                .add(vehicleFilterDto.getRefrigerated(), QVehicle.vehicle.refrigerated::eq)
                .add(vehicleFilterDto.getModel(), QVehicle.vehicle.model::eq)
                .buildAnd();
        return new JPAQuery<>(entityManager)
                .select(QVehicle.vehicle)
                .from(QVehicle.vehicle)
                .join(QVehicle.vehicle.carrier, QUser.user)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), entityManager.getEntityGraph("VehicleWithCarrier"))
                .fetch();
    }
}
