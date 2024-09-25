package com.stepaniuk.workshop.types.order;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonSubTypes({
        @Type(
                name = PerItemPriceProperties.TYPE,
                value = PerItemPriceProperties.class
        )
})
// @formatter:on
@EqualsAndHashCode
@ToString
public abstract class PriceProperties {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    public abstract String getType();
}
