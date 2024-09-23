package com.stepaniuk.workshop.types.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

@JsonTypeName(ComplexTablePrice.TYPE)
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ComplexTablePrice extends Price {

  public static final String TYPE = "complex-table";

  private final Row[] rows;

  @ConstructorProperties({"rows"})
  public ComplexTablePrice(Row[] rows) {
    this.rows = rows;
  }

  public String getType() {
    return TYPE;
  }

  @Getter
  @JsonFormat(shape = JsonFormat.Shape.ARRAY)
  @JsonPropertyOrder({"count", "value"})
  public static class Row {

    private final Integer count;
    private final BigDecimal value;

    @ConstructorProperties({"count", "value"})
    public Row(Integer count, BigDecimal value) {
      this.count = count;
      this.value = value;
    }

  }

}
