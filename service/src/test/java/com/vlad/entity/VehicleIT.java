package com.vlad.entity;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.vlad.TestBase;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;


class VehicleIT extends TestBase {

    @Test
    void getRefrigeratedVehicleWithCarrierQuerydsl() {
        User carrier = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.CARRIER)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(18)
                .refrigerated(true)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        Vehicle vehicle2 = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("7688")
                .capacity(BigDecimal.valueOf(77.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(22)
                .refrigerated(true)
                .model("VW CRAFTER")
                .build();
        session.persist(vehicle2);
        session.flush();
        session.clear();

//        List<Tuple> refrigeratedVehicles = new JPAQuery<>(session)
//                .select(QVehicle.vehicle, QUser.user.name, QUser.user.contactInfo)
//                .from(QVehicle.vehicle)
//                .join(QVehicle.vehicle.carrier, QUser.user)
//                .where(QVehicle.vehicle.refrigerated.eq(true))
//                .fetch();
//
//        Vehicle first = refrigeratedVehicles.get(0).get(QVehicle.vehicle);
//        Vehicle second = refrigeratedVehicles.get(1).get(QVehicle.vehicle);
//
//        assertEquals(2, refrigeratedVehicles.size());
//        assertEquals(vehicle, first);
//        assertEquals(vehicle2, second);

        List<Vehicle> refrigeratedVehicles = new JPAQuery<>(session)
                .select(QVehicle.vehicle)
                .from(QVehicle.vehicle)
                .join(QVehicle.vehicle.carrier, QUser.user)
                .where(QVehicle.vehicle.refrigerated.eq(true))
                .setHint(GraphSemantic.LOAD.getJakartaHintName(),session.getEntityGraph("VehicleWithCarrier"))
                .fetch();

        assertEquals(2, refrigeratedVehicles.size());
        assertEquals(vehicle, refrigeratedVehicles.get(0));
        assertEquals(vehicle2, refrigeratedVehicles.get(1));

    }

    @Test
    void getRefrigeratedVehicleWithCarrierCriteriaApi() {
        User carrier = User.builder()
                .username("MickyMouse")
                .password("1234567")
                .role(Role.CARRIER)
                .name("Viktoria")
                .contactInfo("vika@example.com")
                .address("Mazurova 4")
                .build();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(18)
                .refrigerated(true)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        Vehicle vehicle2 = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("7688")
                .capacity(BigDecimal.valueOf(77.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(22)
                .refrigerated(true)
                .model("VW CRAFTER")
                .build();
        session.persist(vehicle2);
        session.flush();
        session.clear();


//        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
//        JpaCriteriaQuery<Vehicle> criteria = cb.createQuery(Vehicle.class);
//        JpaRoot<Vehicle> newVehicleRoot = criteria.from(Vehicle.class);
//        newVehicleRoot.fetch("carrier");
//        criteria.select(newVehicleRoot).where(cb.isTrue(newVehicleRoot.get("refrigerated")));
//        List<Vehicle> resultList = session.createQuery(criteria).getResultList();
//
//        assertEquals(2, resultList.size());
//        Vehicle firstVehicle = resultList.get(0);
//        Vehicle secondVehicle = resultList.get(1);
//        assertEquals(vehicle, firstVehicle);
//        assertEquals(vehicle2, secondVehicle);

        List<Vehicle> resultList = session.createQuery(
                        "SELECT v FROM Vehicle v WHERE v.refrigerated = true", Vehicle.class)
                .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("VehicleWithCarrier"))
                .getResultList();

        assertFalse(resultList.isEmpty());
        assertEquals(vehicle, resultList.get(0));
        assertEquals(vehicle2, resultList.get(1));

    }

    @Test
    void deleteVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        session.remove(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertNull(actualResult);
    }

    @Test
    void updateVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        vehicle.setLicensePlate("9999");
        session.merge(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals("9999", actualResult.getLicensePlate());
    }

    @Test
    void getVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals(vehicle, actualResult);
    }

    @Test
    void createVehicle() {
        User carrier = getCarrier();
        session.persist(carrier);
        Vehicle vehicle = Vehicle.builder()
                .carrier(carrier)
                .licensePlate("1234")
                .capacity(BigDecimal.valueOf(25.90).setScale(2, RoundingMode.HALF_UP))
                .palletCapacity(18)
                .refrigerated(false)
                .model("ISUZU")
                .build();
        session.persist(vehicle);
        session.flush();
        session.clear();

        Vehicle actualResult = session.get(Vehicle.class, vehicle.getId());

        assertEquals(vehicle, actualResult);
    }

    private static User getCarrier() {
        return User.builder()
                .username("MilkyWay")
                .password("1234567")
                .role(Role.CARRIER)
                .name("Alexandra")
                .contactInfo("alexandra@example.com")
                .address("Lobonka 44")
                .build();
    }

}
