package com.stepaniuk.workshop.payload.order;

import com.stepaniuk.workshop.types.order.OrderStatusName;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderUpdateRequest {

    @Nullable
    private OrderStatusName status;

    @Nullable
    private String comment;

    @Nullable
    private Instant appointmentTime;
}
