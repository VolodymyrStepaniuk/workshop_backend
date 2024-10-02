package com.stepaniuk.workshop.payload.feedback;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FeedbackCreateRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Integer rating;

    @Nullable
    private String comment;
}
