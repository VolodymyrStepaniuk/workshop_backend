package com.stepaniuk.workshop.types.service;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

@JsonTypeName(PerItemPrice.TYPE)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PerItemPrice extends Price {

  public static final String TYPE = "per-item";

  private final BigDecimal value;

  @ConstructorProperties({"value"})
  public PerItemPrice(BigDecimal value) {
    this.value = value;
  }


  public String getType() {
    return TYPE;
  }

}
