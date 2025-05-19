package com.zegasoftware.stock_management.model.dto.stock;

import com.zegasoftware.stock_management.model.enums.StockTypes;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StockDetails {

    private String name;

    private BigDecimal price;

    private int stockOwnerCount;

    private StockTypes stockType;

    private BigDecimal marketCap;

//    private String sector;


}
