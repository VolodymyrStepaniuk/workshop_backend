package com.stepaniuk.workshop.payload.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stepaniuk.workshop.types.order.OrderStatusName;
import com.stepaniuk.workshop.types.order.PriceProperties;
import com.stepaniuk.workshop.types.service.Price;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "orders", itemRelation = "orders")
public class OrderResponse extends RepresentationModel<OrderResponse> {

    @NotNull
    private Long id;

    @NotNull
    private Long customerId;

    @Nullable
    private String comment;

    @NotNull
    private OrderStatusName status;

    @NotNull
    @Size(min = 1)
    private List<Item> items;

    @NotNull
    private Instant appointmentTime;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant lastModifiedAt;

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {

        private Long serviceId;

        private Price price;
        @Nullable
        private PriceProperties priceProperties;
    }
}
