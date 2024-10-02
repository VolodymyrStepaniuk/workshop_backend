package com.stepaniuk.workshop.payload.feedback;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class FeedbackResponse extends RepresentationModel<FeedbackResponse> {

    @NotNull
    private Long id;

    @NotNull
    private Long orderId;

    @NotNull
    private Integer rating;

    @Nullable
    private String comment;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant lastModifiedAt;

    @NotNull
    private List<Long> categories;

    @NotNull
    private User reviewer;

    @Getter
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class User {

        @NotNull
        private Long id;

        @NotNull
        private String firstName;

        @NotNull
        private String lastName;
    }
}
