package com.stepaniuk.workshop.payload.service.category;

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
@Relation(collectionRelation = "serviceCategories", itemRelation = "serviceCategories")
public class ServiceCategoryResponse extends RepresentationModel<ServiceCategoryResponse> {
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private List<String> imageUrls;

    @NotNull
    private Integer priority;

   @NotNull
    private String urlName;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant lastModifiedAt;
}
