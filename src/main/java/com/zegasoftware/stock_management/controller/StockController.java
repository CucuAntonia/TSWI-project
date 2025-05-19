package com.zegasoftware.stock_management.controller;

import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
import com.zegasoftware.stock_management.model.dto.stock.StockSummary;
import com.zegasoftware.stock_management.model.enums.StockTypes;
import com.zegasoftware.stock_management.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/user/stocks")
    public ResponseEntity<List<StockSummary>> getAllStocks() {
        List<StockSummary> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/user/stocks/{id}")
    public ResponseEntity<StockDetails> getStockById(@PathVariable UUID id) {
        return stockService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/stocks/type/{type}")
    public List<StockDetails> getStocksByType(@PathVariable StockTypes type) {
        return stockService.getByStockType(type).stream().toList();
    }

    @GetMapping("/user/stocks/price-above/{price}/date-before/{date}")
    public ResponseEntity<List<StockDetails>> getStocksAbovePriceBeforeDate(
            @PathVariable BigDecimal price,
            @PathVariable LocalDateTime date) {
        List<StockDetails> stocks = stockService.findByPriceAboveAndDateBefore(price, date);
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/user/stocks/market-cap-above/{percentage}")
    public ResponseEntity<List<StockDetails>> getStocksByMarketCapAbove(@PathVariable BigDecimal percentage) {
        List<StockDetails> stocks = stockService.findByMarketCapAbove(percentage);
        return ResponseEntity.ok(stocks);
    }

    @PostMapping("/admin/stocks")
    public ResponseEntity<Boolean> addStocks(@RequestBody StockDetails stock) {
        return stockService.saveStock(stock) ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/admin/stocks/{id}/market-cap")
    public ResponseEntity<Void> updateMarketCap(@PathVariable UUID id, @RequestBody BigDecimal newMarketCap) {
        stockService.updateMarketCap(id, newMarketCap);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/admin/stocks/{id}/type")
    public ResponseEntity<Void> updateStockType(@PathVariable UUID id, @RequestBody StockTypes newType) {
        stockService.updateStockType(id, newType);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/admin/stocks/{id}")
    public ResponseEntity<Void> updateStock(@PathVariable UUID id, @RequestBody StockDetails stockDetails) {
        stockService.updateStock(id, stockDetails);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/stocks/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable UUID id) {
        boolean deleted = stockService.deleteStock(id);
        if (deleted) {
            return ResponseEntity.ok("Stock has been marked as deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock with ID " + id + " not found or already deleted.");
        }
    }
//    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/stocks/owner-count/{count}")
    public ResponseEntity<String> deleteStocksByOwnerCount(@PathVariable int count) {
        boolean deleted = stockService.deleteByOwnerCount(count);
        return deleted ? ResponseEntity.ok("Stock has been marked as deleted. ") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock not found or already deleted.");
    }
}
