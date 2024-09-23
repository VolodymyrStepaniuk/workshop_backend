package com.stepaniuk.workshop.types.service;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

@JsonTypeName(FixedPrice.TYPE)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FixedPrice extends Price {

  public static final String TYPE = "fixed";

  private final BigDecimal value;

  @ConstructorProperties({"value"})
  public FixedPrice(BigDecimal value) {
    this.value = value;
  }


  public String getType() {
    return TYPE;
  }

}
