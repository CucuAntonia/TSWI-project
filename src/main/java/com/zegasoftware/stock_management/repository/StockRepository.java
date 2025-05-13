package com.zegasoftware.stock_management.repository;

import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
import com.zegasoftware.stock_management.model.dto.stock.StockSummary;
import com.zegasoftware.stock_management.model.entity.Stock;
import com.zegasoftware.stock_management.model.enums.StockTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    @Query("select new com.zegasoftware.stock_management.model.dto.stock.StockDetails" +
            "(s.name, s.price, s.stockOwnerCount, s.stockType, s.marketCap) from Stock s where s.stockType=:type and s.isDeleted = false")
    List<StockDetails> findByStockType(@Param("type") StockTypes type);

    @Query("select new com.zegasoftware.stock_management.model.dto.stock.StockDetails" +
            "(s.name, s.price, s.stockOwnerCount, s.stockType, s.marketCap) from Stock s where s.id=:id and s.isDeleted = false")
    Optional<StockDetails> findByStockId(@Param("id") UUID id);

    Optional<Stock> findByIdAndIsDeletedFalse(UUID id);

    @Query("select new com.zegasoftware.stock_management.model.dto.stock.StockSummary" +
            "(s.name, s.price, s.stockType ) from Stock s where s.isDeleted = false")
    List<StockSummary> getAllStocks();

    @Query("select s from Stock s where s.isDeleted = false")
    List<Stock> findAllStocks();
}
