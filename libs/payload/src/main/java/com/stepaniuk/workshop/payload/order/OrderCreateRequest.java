package com.stepaniuk.workshop.payload.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stepaniuk.workshop.types.order.PriceProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderCreateRequest {

    @NotNull
    @Size(min = 1)
    private List<Item> lineItems;

    @Nullable
    private String comment;

    @NotNull
    private Instant appointmentTime;

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {

        private Long serviceId;
        @Nullable
        private PriceProperties priceProperties;
    }
}
