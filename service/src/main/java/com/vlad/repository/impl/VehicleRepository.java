package com.vlad.repository.impl;

import com.querydsl.jpa.impl.JPAQuery;
import com.vlad.dto.filter.VehicleFilterDto;
import com.vlad.entity.QUser;
import com.vlad.entity.QVehicle;
import com.vlad.entity.Vehicle;
import com.vlad.repository.AbstractRepository;
import com.vlad.repository.QPredicate;
import jakarta.persistence.EntityManager;
import org.hibernate.graph.GraphSemantic;

import java.util.List;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleRepository extends AbstractRepository<Long, Vehicle> {

    public VehicleRepository(EntityManager entityManager) {
        super(Vehicle.class, entityManager);
    }

    public List<Vehicle> getVehicleByFilter(VehicleFilterDto filterDto) {
        Predicate predicate = QPredicate.builder()
                .add(filterDto.getPalletCapacity(), QVehicle.vehicle.palletCapacity::eq)
                .add(filterDto.getRefrigerated(), QVehicle.vehicle.refrigerated::eq)
                .add(filterDto.getModel(), QVehicle.vehicle.model::eq)
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
