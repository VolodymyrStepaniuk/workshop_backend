package com.stepaniuk.workshop.order.status;

import com.stepaniuk.workshop.testspecific.JpaLevelTest;
import com.stepaniuk.workshop.types.order.OrderStatusName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JpaLevelTest
@Sql(scripts = "classpath:sql/order_statuses.sql")
public class OrderStatusRepositoryTest {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Test
    void shouldSaveOrderStatus() {
        OrderStatus orderStatus = new OrderStatus(
                null, OrderStatusName.CREATED, 100
        );

        OrderStatus savedStatus = orderStatusRepository.save(orderStatus);

        assertNotNull(savedStatus);
        assertNotNull(savedStatus.getId());
        assertEquals(orderStatus.getName(), savedStatus.getName());
        assertEquals(orderStatus.getPriority(), savedStatus.getPriority());
    }

    @Test
    void shouldThrowExceptionWhenSavingOrderStatus() {
        OrderStatus orderStatus = new OrderStatus(
                null, null, 100
        );

        assertThrows(DataIntegrityViolationException.class, () -> orderStatusRepository.save(orderStatus));
    }

    @Test
    void shouldFindOrderStatusById() {
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(1L);
        assertTrue(optionalOrderStatus.isPresent());

        OrderStatus orderStatus = optionalOrderStatus.get();
        assertNotNull(orderStatus);
        assertEquals(OrderStatusName.CREATED, orderStatus.getName());
        assertEquals(100, orderStatus.getPriority());
    }

    @Test
    void shouldUpdateOrderStatus() {
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(1L);
        assertTrue(optionalOrderStatus.isPresent());

        OrderStatus orderStatus = optionalOrderStatus.get();

        orderStatus.setName(OrderStatusName.CANCELED);
        OrderStatus updatedOrderStatus = orderStatusRepository.save(orderStatus);
        assertNotNull(updatedOrderStatus);

        assertEquals(orderStatus.getId(), updatedOrderStatus.getId());
        assertEquals(OrderStatusName.CANCELED, updatedOrderStatus.getName());
        assertEquals(orderStatus.getPriority(), updatedOrderStatus.getPriority());
    }

    @Test
    void shouldDeleteOrderStatusByExistingOrderStatus() {
        Optional<OrderStatus> optionalOrderStatus = orderStatusRepository.findById(1L);
        assertTrue(optionalOrderStatus.isPresent());

        OrderStatus orderStatus = optionalOrderStatus.get();
        orderStatusRepository.delete(orderStatus);

        assertFalse(orderStatusRepository.existsById(1L));
    }

    @Test
    void shouldDeleteOrderStatusById(){
        orderStatusRepository.deleteById(1L);

        assertFalse(orderStatusRepository.existsById(1L));
    }

    @Test
    void shouldReturnListOfOrderStatuses(){
        List<OrderStatus> orderStatuses = orderStatusRepository.findAll();

        assertNotNull(orderStatuses);
        assertFalse(orderStatuses.isEmpty());
    }

    @Test
    void shouldReturnPageOfOrderStatuses(){
        Page<OrderStatus> orderStatusPage = orderStatusRepository.findAll(PageRequest.of(0,5));

        assertNotNull(orderStatusPage);
        assertFalse(orderStatusPage.isEmpty());

        List<OrderStatus> orderStatuses = orderStatusPage.getContent();
        assertNotNull(orderStatuses);
        assertFalse(orderStatuses.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {300, 400, 400})
    void shouldReturnListOfOrderStatusesWherePriorityIsUnderOrEqualToParam(int priority){
        Specification<OrderStatus> orderStatusSpecification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("priority"),priority)
        );

        List<OrderStatus> orderStatuses = orderStatusRepository.findAll(orderStatusSpecification);
        assertNotNull(orderStatuses);
        assertFalse(orderStatuses.isEmpty());
        assertAll(orderStatuses.stream().map(
                status -> () -> assertTrue(
                        status.getPriority() <= priority
                )
        ));
    }
}
