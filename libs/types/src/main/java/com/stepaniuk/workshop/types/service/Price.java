package com.stepaniuk.workshop.types.service;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// @formatter:off
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonSubTypes({
    @Type(
        name = FixedPrice.TYPE,
        value = FixedPrice.class
    ),
    @Type(
        name = RangedPrice.TYPE,
        value = RangedPrice.class
    ),
    @Type(
        name = PerItemPrice.TYPE,
        value = PerItemPrice.class
    )
})
// @formatter:on
@EqualsAndHashCode
@ToString
public abstract class Price {

  public abstract String getType();

}
