package com.vlad.repository;

import com.vlad.annotation.IT;
import com.vlad.dto.filter.RequestFilterDto;
import com.vlad.entity.Request;
import com.vlad.entity.RequestStatus;
import com.vlad.entity.Role;
import com.vlad.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.jupiter.api.Test;
import org.springframework.data.history.Revisions;
import org.springframework.test.annotation.Commit;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class RequestRepositoryIT {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Test
    void checkAuditing() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);
        requestRepository.save(request);
        entityManager.flush();
        request.setStatus(RequestStatus.COMPLETED);

        requestRepository.saveAndFlush(request);
        entityManager.clear();

        Optional<Request> actualResult = requestRepository.findById(request.getId());
        assertEquals(actualResult.get().getStatus(), RequestStatus.COMPLETED);

        Revisions<Long, Request> revisions = requestRepository.findRevisions(request.getId());
        System.out.println();

    }

    @Test
    void getRequestByFilter() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = new Request();
        request.setCustomer(customer);
        request.setStatus(RequestStatus.IN_PROGRESS);
        request.setCargoDetails("Intercars");
        request.setWeight(BigDecimal.valueOf(1800.90).setScale(2, RoundingMode.HALF_UP));
        request.setPalletCount(15);
        request.setRefrigerated(true);
        request.setPickupAddress("Yanki 33");
        request.setDeliveryAddress("Masherova 2");
        request.setCreationDate(LocalDate.now());
        request.setCarrier(carrier);
        Request request1 = getRequest(customer, carrier);
        Request request2 = getRequest(customer, carrier);
        requestRepository.save(request);
        requestRepository.save(request1);
        requestRepository.save(request2);
        RequestFilterDto filter = RequestFilterDto.builder()
                .status(RequestStatus.IN_PROGRESS)
                .pickupAddress("Yanki 33")
                .deliveryAddress("Masherova 2")
                .build();

        List<Request> actualResult = requestRepository.findAllByFilter(filter);

        assertEquals(request, actualResult.get(0));
    }

    @Test
    void deleteRequest() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);
        requestRepository.save(request);

        requestRepository.delete(request);

        Optional<Request> actualResult = requestRepository.findById(request.getId());
        assertFalse(actualResult.isPresent());
    }

    @Test
    void updateRequest() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);
        requestRepository.save(request);
        request.setStatus(RequestStatus.IN_PROGRESS);

        requestRepository.save(request);

        Optional<Request> actualResult = requestRepository.findById(request.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(RequestStatus.IN_PROGRESS, actualResult.get().getStatus());
    }

    @Test
    void getAllRequests() {
        User customer1 = getCustomer();
        userRepository.save(customer1);
        User carrier1 = getCarrier();
        userRepository.save(carrier1);
        Request request = getRequest(customer1, carrier1);
        requestRepository.save(request);
        User customer2 = getCustomer1();
        userRepository.save(customer2);
        User carrier2 = getCarrier1();
        userRepository.save(carrier2);
        Request request2 = new Request();
        request2.setCustomer(customer2);
        request2.setStatus(RequestStatus.IN_PROGRESS);
        request2.setCargoDetails("Some Cargo Details");
        request2.setWeight(BigDecimal.valueOf(1800.40).setScale(2, RoundingMode.HALF_UP));
        request2.setPalletCount(20);
        request2.setRefrigerated(true);
        request2.setPickupAddress("Pushkina 34");
        request2.setDeliveryAddress("Kasperskogo 33");
        request2.setCreationDate(LocalDate.now());
        request2.setCarrier(carrier2);
        requestRepository.save(request2);

        List<Request> actualResult = requestRepository.findAll();

        assertEquals(2, actualResult.size());
        assertThat(actualResult).containsExactlyInAnyOrder(request, request2);
    }

    @Test
    void saveRequest() {
        User customer = getCustomer();
        userRepository.save(customer);
        User carrier = getCarrier();
        userRepository.save(carrier);
        Request request = getRequest(customer, carrier);

        requestRepository.save(request);

        Optional<Request> actualResult = requestRepository.findById(request.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(request, actualResult.get());
    }

    private static Request getRequest(User customer, User carrier) {
        Request request = new Request();
        request.setCustomer(customer);
        request.setStatus(RequestStatus.PENDING);
        request.setCargoDetails("Cargo Details");
        request.setWeight(BigDecimal.valueOf(2500.90).setScale(2, RoundingMode.HALF_UP));
        request.setPalletCount(25);
        request.setRefrigerated(false);
        request.setPickupAddress("Kolasa 45");
        request.setDeliveryAddress("Selitskogo 21");
        request.setCreationDate(LocalDate.now());
        request.setCarrier(carrier);
        return request;
    }

    private static User getCarrier1() {
        User user = new User();
        user.setUsername("Pumba");
        user.setPassword("534534543");
        user.setRole(Role.CARRIER);
        user.setName("Papo4ka");
        user.setContactInfo("papa@gmail.com");
        user.setAddress("Beruta 5");
        return user;
    }

    private static User getCustomer1() {
        User user = new User();
        user.setUsername("King");
        user.setPassword("Kingforever");
        user.setRole(Role.CUSTOMER);
        user.setName("King");
        user.setContactInfo("king@gmail.com");
        user.setAddress("Pritickogo 23");
        return user;
    }

    private static User getCarrier() {
        User user = new User();
        user.setUsername("Timon");
        user.setPassword("999999");
        user.setRole(Role.CARRIER);
        user.setName("Twinki");
        user.setContactInfo("tv@gmail.com");
        user.setAddress("Selickogo 21");
        return user;
    }

    private static User getCustomer() {
        User user = new User();
        user.setUsername("MickyMouse");
        user.setPassword("123MickyMouse123");
        user.setRole(Role.CUSTOMER);
        user.setName("Martin");
        user.setContactInfo("martin@gmail.com");
        user.setAddress("Mazurova 4-56");
        return user;
    }
}
