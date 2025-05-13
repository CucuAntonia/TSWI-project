package com.zegasoftware.stock_management.mapper;

import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
import com.zegasoftware.stock_management.model.dto.stock.StockSummary;
import com.zegasoftware.stock_management.model.entity.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMapper {

    Stock toEntity(StockDetails stockDetails);
    Stock toEntity(StockSummary stockSummary);
}
