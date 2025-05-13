package com.zegasoftware.stock_management.model.dto.stock;

import com.zegasoftware.stock_management.model.enums.StockTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockSummary {

    private String name;

    private BigDecimal price;

    private StockTypes stockType;

    public StockSummary(String name, BigDecimal price){
        this.name = name;
        this.price = price;
    }
}
