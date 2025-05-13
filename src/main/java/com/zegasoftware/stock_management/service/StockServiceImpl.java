package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.mapper.StockMapper;
import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
import com.zegasoftware.stock_management.model.dto.stock.StockSummary;
import com.zegasoftware.stock_management.model.entity.Stock;
import com.zegasoftware.stock_management.model.enums.StockTypes;
import com.zegasoftware.stock_management.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepo;

    private final StockMapper mapper;

    private static final String STOCK_NOT_FOUND = "Stock not found with ID: ";

    /**
     *
     * If I use @AllArgsConstructor annotation, will it know to use @Autowired?
     *
     */
//    @Autowired
//    public StockServiceImpl(StockRepository stockRepo, StockMapper mapper) {
//        this.stockRepo = stockRepo;
//        this.mapper = mapper;
//    }

    @Override
    public List<StockSummary> getAllStocks() {
        return stockRepo.getAllStocks();
    }

    @Override
    public Optional<StockDetails> getById(UUID id) {
        return stockRepo.findByStockId(id);
    }

    @Override
    public List<StockDetails> getByStockType(StockTypes type) {
        return stockRepo.findByStockType(type);
    }

    @Override
    public boolean saveStock(StockDetails stock) {
        if (stock == null) {
            return false;
        }

        stockRepo.save(mapper.toEntity(stock));
        return true;
    }

    @Override
    public boolean deleteStock(UUID id) {
        return stockRepo.findByStockId(id)
                .map(stock -> {
                    Stock newStock = mapper.toEntity(stock);
                    if (!newStock.isDeleted()) {
                        newStock.setId(id);
                        newStock.setDeleted(true);
                        stockRepo.save(newStock);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public void updateStock(UUID id, StockDetails stockDetails) {
        if (id == null) {
            throw new EntityNotFoundException("Stock ID must not be null");
        }

        if (stockDetails == null) {
            throw new IllegalArgumentException("Stock details must not be null");
        }

        stockRepo.findByIdAndIsDeletedFalse(id)
                .ifPresent(stock -> {
                    stock.setName(stockDetails.getName());
                    stock.setPrice(stockDetails.getPrice());
                    stock.setStockOwnerCount(stockDetails.getStockOwnerCount());
                    stock.setStockType(stockDetails.getStockType());
                    stock.setMarketCap(stockDetails.getMarketCap());
                    stockRepo.save(stock);
                });
    }

    @Override
    public List<StockDetails> findByPriceAboveAndDateBefore(BigDecimal price, LocalDateTime date) {
        if (price == null) {
            throw new IllegalArgumentException("Price must not be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        return stockRepo.findAllStocks().stream()
                .filter(stock -> stock.getPrice().compareTo(price) > 0 && stock.getStockCreation().isBefore(date))
                .map(stock -> new StockDetails(
                        stock.getName(),
                        stock.getPrice(),
                        stock.getStockOwnerCount(),
                        stock.getStockType(),
                        stock.getMarketCap()))
                .toList();
    }

    @Override
    public List<StockDetails> findByMarketCapAbove(BigDecimal percentage) {
        if (percentage == null) {
            throw new IllegalArgumentException("Market cap must not be null");
        }
        return stockRepo.findAllStocks().stream()
                .filter(stock -> stock.getMarketCap().compareTo(percentage) > 0)
                .map(stock -> new StockDetails(
                        stock.getName(),
                        stock.getPrice(),
                        stock.getStockOwnerCount(),
                        stock.getStockType(),
                        stock.getMarketCap()))
                .toList();
    }

    @Override
    public void updateMarketCap(UUID id, BigDecimal newMarketCap) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (newMarketCap == null) {
            throw new IllegalArgumentException("Market cap must not be null");
        }
        Stock stock = stockRepo.findByStockId(id).map(mapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException(STOCK_NOT_FOUND + id));
        stock.setId(id);
        stock.setMarketCap(newMarketCap);
        stock.setStockCreation(stock.getStockCreation());
        stockRepo.save(stock);
    }

    @Override
    public void updateStockType(UUID id, StockTypes newType) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (newType == null) {
            throw new IllegalArgumentException("Type must not be null");
        }
        Stock stock = stockRepo.findByStockId(id).map(mapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException(STOCK_NOT_FOUND + id));
        stock.setId(id);
        stock.setStockType(newType);
        stock.setStockCreation(stock.getStockCreation());
        stockRepo.save(stock);
    }

    @Override
    public boolean deleteByOwnerCount(int ownerCount) {
        List<Stock> stocksToDelete = stockRepo.findAllStocks().stream()
                .filter(stock -> stock.getStockOwnerCount() == ownerCount)
                .toList();

        if (stocksToDelete.isEmpty()) {
            return false;
        }

        for (Stock stock : stocksToDelete) {
            stock.setId(stock.getId());
            stock.setDeleted(true);
            stockRepo.save(stock);
        }

        return true;
    }

}
