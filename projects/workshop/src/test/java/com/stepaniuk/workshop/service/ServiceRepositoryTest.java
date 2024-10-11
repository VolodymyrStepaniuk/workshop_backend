package com.stepaniuk.workshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import com.stepaniuk.workshop.testspecific.JpaLevelTest;
import com.stepaniuk.workshop.types.service.FixedPrice;
import com.stepaniuk.workshop.types.service.PerItemPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@JpaLevelTest
@Sql(scripts = "classpath:sql/services.sql")
public class ServiceRepositoryTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @Test
    void shouldSaveService() {
        FixedPrice price = new FixedPrice(BigDecimal.valueOf(1000));

        Service service = new Service(null, 1L, "Title", "Description",
                List.of("image_url"), 100, price, null, null);

        Service savedService = serviceRepository.save(service);

        assertNotNull(savedService);
        assertNotNull(savedService.getId());

        assertEquals(service.getCategoryId(), savedService.getCategoryId());
        assertEquals(service.getTitle(), savedService.getTitle());
        assertEquals(service.getDescription(), savedService.getDescription());
        assertEquals(service.getImageUrls(), savedService.getImageUrls());
        assertEquals(service.getImageUrls().get(0), savedService.getImageUrls().get(0));
        assertEquals(service.getPrice(), savedService.getPrice());

        FixedPrice fixedPrice = (FixedPrice) savedService.getPrice();

        assertNotNull(fixedPrice);
        assertEquals(price.getType(), fixedPrice.getType());
        assertThat(fixedPrice.getValue()).isEqualByComparingTo(price.getValue());
    }

    @Test
    void shouldThrowExceptionWhenSavingService(){
        FixedPrice price = new FixedPrice(BigDecimal.valueOf(1000));

        Service service = new Service(null, 1L, null, "Description",
                List.of("image_url"), 100, price, null, null);

        assertThrows(DataIntegrityViolationException.class, () -> serviceRepository.save(service));
    }

    @Test
    void shouldFindServiceById() {
        Optional<Service> optionalService = serviceRepository.findById(1L);
        assertTrue(optionalService.isPresent());

        Service service = optionalService.get();
        assertNotNull(service);

        assertEquals(1L, service.getId());
        assertEquals(1L, service.getCategoryId());
        assertEquals("Title", service.getTitle());
        assertEquals("Description", service.getDescription());
        assertEquals(List.of("image_url"), service.getImageUrls());
        assertEquals("image_url", service.getImageUrls().get(0));
        assertEquals(100, service.getPriority());
        assertEquals(Instant.parse("2024-10-24T16:22:09.266615Z"), service.getCreatedAt());
        assertEquals(Instant.parse("2024-10-25T17:28:19.266615Z"), service.getLastModifiedAt());

        PerItemPrice fixedPrice = (PerItemPrice) service.getPrice();
        assertNotNull(fixedPrice);
        assertEquals("per-item", fixedPrice.getType());
        assertThat(fixedPrice.getValue()).isEqualByComparingTo(BigDecimal.valueOf(250));
    }

    @Test
    void shouldUpdateService() {
        Optional<Service> optionalService = serviceRepository.findById(1L);
        assertTrue(optionalService.isPresent());

        Service service = optionalService.get();
        assertNotNull(service);

        FixedPrice fixedPrice = new FixedPrice(BigDecimal.valueOf(500));

        service.setPrice(fixedPrice);

        Service savedService = serviceRepository.save(service);

        assertNotNull(savedService);
        assertEquals(service.getId(), savedService.getId());

        FixedPrice servicePrice = (FixedPrice) savedService.getPrice();
        assertNotNull(servicePrice);
        assertThat(servicePrice.getValue()).isEqualByComparingTo(fixedPrice.getValue());
    }

    @Test
    void shouldDeleteServiceByExistingService(){
        Optional<Service> optionalService = serviceRepository.findById(1L);
        assertTrue(optionalService.isPresent());

        Service service = optionalService.get();
        assertNotNull(service);

        serviceRepository.delete(service);

        assertFalse(serviceRepository.existsById(1L));
    }

    @Test
    void shouldDeleteServiceById(){
        serviceRepository.deleteById(1L);

        assertFalse(serviceRepository.existsById(1L));
    }

    @Test
    void shouldReturnListOfAllServices() {
        List<Service> services = serviceRepository.findAll();

        assertNotNull(services);
        assertFalse(services.isEmpty());
    }

    @Test
    void shouldReturnPageOfAllServices() {
        Page<Service> servicePage = serviceRepository.findAll(PageRequest.of(0,2));

        assertNotNull(servicePage);
        assertFalse(servicePage.isEmpty());

        List<Service> serviceList = servicePage.getContent();

        assertNotNull(serviceList);
        assertFalse(serviceList.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    void shouldReturnListOfServicesByCategoryId(int categoryId) {
        Specification<Service> serviceSpecification = Specification.where(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoryId"), categoryId)
        );

        List<Service> serviceList = serviceRepository.findAll(serviceSpecification);
        assertNotNull(serviceList);
        assertFalse(serviceList.isEmpty());

        assertAll(
                serviceList.stream().map(
                        service -> () -> assertEquals(categoryId, service.getCategoryId())
                )
        );
    }
}
