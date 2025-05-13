package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
import com.zegasoftware.stock_management.model.dto.stock.StockSummary;
import com.zegasoftware.stock_management.model.enums.StockTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockService {

    List<StockSummary> getAllStocks();

    Optional<StockDetails> getById(UUID id);

    List<StockDetails> getByStockType(StockTypes type);

    boolean saveStock(StockDetails stock);

    boolean deleteStock(UUID id);

    void updateStock(UUID id, StockDetails stockDetails);

    List<StockDetails> findByPriceAboveAndDateBefore(BigDecimal price, LocalDateTime date);

    List<StockDetails> findByMarketCapAbove(BigDecimal percentage);

    void updateMarketCap(UUID id, BigDecimal newMarketCap);

    void updateStockType(UUID id, StockTypes newType);

    boolean deleteByOwnerCount(int ownerCount);
}
