package com.stepaniuk.workshop.payload.service;

import com.stepaniuk.workshop.types.service.Price;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "services", itemRelation = "service")
public class ServiceResponse extends RepresentationModel<ServiceResponse> {

    @NotNull
    private Long id;

    @NotNull
    private Long categoryId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private List<String> imageUrls;

    @NotNull
    private Integer priority;

    @NotNull
    private Price price;

    @NotNull
    private final Instant createdAt;

    @NotNull
    private final Instant lastModifiedAt;
}
