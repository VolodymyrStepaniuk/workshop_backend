package com.stepaniuk.workshop.types.order;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;

@JsonTypeName(PerItemPriceProperties.TYPE)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PerItemPriceProperties extends PriceProperties {

    public static final String TYPE = "per-item";

    private final int quantity;

    @ConstructorProperties({"quantity"})
    public PerItemPriceProperties(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
