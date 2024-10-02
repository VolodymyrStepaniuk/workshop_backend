package com.stepaniuk.workshop.payload.feedback;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FeedbackUpdateRequest {

    @Nullable
    private Integer rating;

    @Nullable
    private String comment;
}
