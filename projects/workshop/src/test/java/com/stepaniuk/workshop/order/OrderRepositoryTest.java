package com.stepaniuk.workshop.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.stepaniuk.workshop.order.status.OrderStatus;
import com.stepaniuk.workshop.testspecific.JpaLevelTest;
import com.stepaniuk.workshop.types.order.OrderStatusName;
import com.stepaniuk.workshop.types.service.FixedPrice;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@JpaLevelTest
@Sql(scripts = {"classpath:sql/order_statuses.sql", "classpath:sql/orders.sql",
        "classpath:sql/order_items.sql"})
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveOrder(){
        OrderItem orderItem = new OrderItem(
                null, null, null, 1L, new FixedPrice(BigDecimal.valueOf(1500)), null
        );
        OrderStatus orderStatus = new OrderStatus(1L, OrderStatusName.CREATED, 100);

        Order order = new Order(
            null, 1L, "New Comment", orderStatus, Instant.now().plus(Duration.ofHours(3)),
                Set.of(orderItem), null, null
        );

        orderItem.setOrder(order);

        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());
        assertEquals(order.getCustomerId(), savedOrder.getCustomerId());
        assertEquals(order.getStatus(), savedOrder.getStatus());
        assertEquals(order.getAppointmentTime(), savedOrder.getAppointmentTime());
        assertEquals(order.getComment(), savedOrder.getComment());

        Set<OrderItem> orderItems = savedOrder.getOrderItems();
        assertNotNull(orderItems);
        assertFalse(orderItems.isEmpty());

        OrderItem savedOrderItem = orderItems.iterator().next();
        assertNotNull(savedOrderItem);
        assertEquals(orderItem.getServiceId(), savedOrderItem.getServiceId());

        FixedPrice price = (FixedPrice) savedOrderItem.getPrice();
        assertNotNull(price);
        assertThat(price.getValue()).isEqualByComparingTo(BigDecimal.valueOf(1500));
    }

    @Test
    void shouldThrowExceptionWhenSavingOrder(){
        OrderStatus orderStatus = new OrderStatus(1L, OrderStatusName.CREATED, 100);

        Order order = new Order(
                null, null, "New Comment", orderStatus, Instant.now().plus(Duration.ofHours(3)),
                Set.of(), null, null
        );

        assertThrows(DataIntegrityViolationException.class, () -> orderRepository.save(order));
    }

    @Test
    void shouldFindOrderById(){
        Optional<Order> optionalOrder = orderRepository.findById(1L);
        assertTrue(optionalOrder.isPresent());

        Order order = optionalOrder.get();
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(1L, order.getCustomerId());
        assertEquals(OrderStatusName.CREATED, order.getStatus().getName());
        assertEquals("New Comment", order.getComment());
        assertEquals(Instant.parse("2023-11-23T15:22:09.266615Z"), order.getAppointmentTime());
        assertEquals(Instant.parse("2023-11-24T16:22:09.266615Z"), order.getCreatedAt());
        assertEquals(Instant.parse("2023-11-25T17:28:19.266615Z"), order.getLastModifiedAt());

        Set<OrderItem> orderItems = order.getOrderItems();
        assertNotNull(orderItems);
        assertFalse(orderItems.isEmpty());
        OrderItem orderItem = orderItems.iterator().next();
        assertNotNull(orderItem);
        assertEquals(1L, orderItem.getId());
        assertEquals(1L, orderItem.getServiceId());
        assertEquals(1L, orderItem.getOrderId());

        FixedPrice price = (FixedPrice) orderItem.getPrice();
        assertNotNull(price);
        assertThat(price.getValue()).isEqualByComparingTo(BigDecimal.valueOf(1500));
    }

    @Test
    void shouldUpdateOrder(){
        OrderStatus orderStatus = new OrderStatus(2L, OrderStatusName.CONFIRMED, 200);

        Optional<Order> optionalOrder = orderRepository.findById(1L);
        assertTrue(optionalOrder.isPresent());

        Order order = optionalOrder.get();
        assertNotNull(order);

        order.setStatus(orderStatus);
        Order savedOrder = orderRepository.save(order);
        assertNotNull(savedOrder);

        assertEquals(order.getId(), savedOrder.getId());
        assertEquals(orderStatus, savedOrder.getStatus());
    }

    @Test
    void shouldDeleteOrderWhenDeletingByExistingOrder() {
        Optional<Order> optionalOrder = orderRepository.findById(1L);
        assertTrue(optionalOrder.isPresent());

        Order order = optionalOrder.get();
        assertNotNull(order);

        orderRepository.delete(order);

        assertFalse(orderRepository.existsById(1L));
    }

    @Test
    void shouldDeleteOrderWhenDeletingById(){
        orderRepository.deleteById(1L);
        assertFalse(orderRepository.existsById(1L));
    }

    @Test
    void shouldReturnNonEmptyListWhenFindingAllOrders(){
        List<Order> orders = orderRepository.findAll();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    void shouldReturnNonEmptyPageWhenFindingAllOrders(){
        Page<Order> orderPage = orderRepository.findAll(
                PageRequest.of(0, 2)
        );

        assertNotNull(orderPage);
        assertFalse(orderPage.isEmpty());

        List<Order> orderList = orderPage.getContent();
        assertNotNull(orderList);
        assertFalse(orderList.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void shouldReturnListOfOrdersThatHaveGivenCustomerId(int customerId){
        Specification<Order> orderSpecification = Specification.where(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerId"), customerId)
        );

        List<Order> orders = orderRepository.findAll(orderSpecification);
        assertNotNull(orders);
        assertFalse(orders.isEmpty());

        assertAll(
                orders.stream().map(
                        order -> () -> assertEquals(customerId, order.getCustomerId())
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREATED", "CANCELED"})
    void shouldReturnPageOfOrdersThatHaveGivenStatus(String status){
        OrderStatusName orderStatusName = OrderStatusName.valueOf(status);

        Specification<Order> orderSpecification = Specification.where(
                (root, query, criteriaBuilder) -> {
                    Join<Order, OrderStatus> orderStatusesJoin = root.join("status");

                    Path<OrderStatusName> statusName = orderStatusesJoin.get("name");

                    return criteriaBuilder.equal(statusName, orderStatusName);
                }
        );

        Page<Order> pageOfOrders = orderRepository.findAll(orderSpecification, PageRequest.of(0, 2));
        assertNotNull(pageOfOrders);
        assertFalse(pageOfOrders.isEmpty());

        List<Order> orderList = pageOfOrders.getContent();
        assertNotNull(orderList);
        assertFalse(orderList.isEmpty());

        assertAll(
                orderList.stream().map(
                        order -> () -> assertEquals(orderStatusName, order.getStatus().getName())
                )
        );
    }
}
