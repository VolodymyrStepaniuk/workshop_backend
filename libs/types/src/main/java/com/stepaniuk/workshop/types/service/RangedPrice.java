package com.stepaniuk.workshop.types.service;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

@JsonTypeName(RangedPrice.TYPE)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RangedPrice extends Price {

  public static final String TYPE = "range";

  private final BigDecimal from;

  @Nullable
  private final BigDecimal to;

  @ConstructorProperties({"from", "to"})
  public RangedPrice(BigDecimal from, @Nullable BigDecimal to) {
    this.from = from;
    this.to = to;
  }

  public String getType() {
    return TYPE;
  }

}
