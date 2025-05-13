package com.zegasoftware.stock_management.service;

import com.zegasoftware.stock_management.mapper.StockMapper;
import com.zegasoftware.stock_management.model.dto.stock.StockDetails;
import com.zegasoftware.stock_management.model.dto.stock.StockSummary;
import com.zegasoftware.stock_management.model.entity.Stock;
import com.zegasoftware.stock_management.model.enums.StockTypes;
import com.zegasoftware.stock_management.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @InjectMocks
    private StockServiceImpl stockService;

    @Mock
    private StockRepository stockRepo;

    @Mock
    private StockMapper mapper;

    private static final StockDetails STOCK_DETAILS;
    private static final Stock STOCK_ENTITY;

    static {
        STOCK_DETAILS =
                new StockDetails(
                        "Stock",
                        BigDecimal.valueOf(10),
                        5,
                        StockTypes.COMMON,
                        BigDecimal.valueOf(15),
        LocalDateTime.now());
        STOCK_ENTITY = new Stock();
        STOCK_ENTITY.setName("StockEntity");
        STOCK_ENTITY.setPrice(BigDecimal.valueOf(15));
        STOCK_ENTITY.setStockOwnerCount(10);
        STOCK_ENTITY.setStockType(StockTypes.GROWTH);
        STOCK_ENTITY.setMarketCap(BigDecimal.valueOf(20));
        STOCK_ENTITY.setStockCreation(LocalDateTime.now().minusDays(20));
    }

    @Test
    void whenCallingGetAllStocks_thenReturnAllStocks() {

        List<StockSummary> summaries = List.of(
                new StockSummary("Stock1", BigDecimal.valueOf(10), StockTypes.COMMON),
                new StockSummary("Stock2", BigDecimal.valueOf(12), StockTypes.GROWTH)
        );

        when(stockRepo.getAllStocks()).thenReturn(summaries);

        List<StockSummary> actualStocks = stockRepo.getAllStocks();
        assertEquals(2, actualStocks.size());
        assertEquals(summaries, actualStocks);
    }

    @Test
    void givenValidId_whenCallingGetById_thenReturnOptionalStockDetails() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.of(STOCK_DETAILS));
        Optional<StockDetails> stockDetails = stockService.getById(id);
        assertTrue(stockDetails.isPresent());
        assertEquals("Stock", stockDetails.get().getName());
        assertEquals(BigDecimal.valueOf(10), stockDetails.get().getPrice());
    }

    @Test
    void givenInvalidInput_whenCallingGetById_thenReturnEmptyOptional() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.empty());
        Optional<StockDetails> stockDetails = stockService.getById(id);
        assertTrue(stockDetails.isEmpty());
    }

    @Test
    void givenValidStockType_whenCallingGetByStockType_thenReturnListStockDetails() {
         List<StockDetails> detailsList = List.of(STOCK_DETAILS, STOCK_DETAILS);
         when(stockRepo.findByStockType(StockTypes.COMMON)).thenReturn(detailsList);
         List<StockDetails> actualDetails = stockService.getByStockType(StockTypes.COMMON);
         assertEquals(2, actualDetails.size());
         assertEquals(detailsList, actualDetails);
    }

    @Test
    void givenNullInput_whenCallingGetByStockType_thenReturnEmptyList() {
        when(stockRepo.findByStockType(null)).thenReturn(Collections.emptyList());
        List<StockDetails> stockDetails = stockService.getByStockType(null);
        assertEquals(Collections.emptyList(), stockDetails);
    }

    @Test
    void givenValidStockDetails_whenCallingSaveStock_thenReturnTrue() {
        when(mapper.toEntity(STOCK_DETAILS)).thenReturn(STOCK_ENTITY);
        when(stockRepo.save(STOCK_ENTITY)).thenReturn(STOCK_ENTITY);
        assertTrue(stockService.saveStock(STOCK_DETAILS));
    }

    @Test
    void givenInvalidStockDetails_whenCallingSaveStock_thenReturnFalse() {
        assertFalse(stockService.saveStock(null));
    }

    @Test
    void givenValidId_whenCallingDeleteStock_andIsNotDeleted_thenReturnTrue() {
        UUID id = UUID.randomUUID();
        STOCK_ENTITY.setDeleted(false);
        when(stockRepo.findByStockId(id)).thenReturn(Optional.of(STOCK_DETAILS));
        when(mapper.toEntity(STOCK_DETAILS)).thenReturn(STOCK_ENTITY);
        when(stockRepo.save(STOCK_ENTITY)).thenReturn(STOCK_ENTITY);

        boolean result = stockService.deleteStock(id);
        assertTrue(result);
        assertTrue(STOCK_ENTITY.isDeleted());
    }

    @Test
    void givenValidId_whenCallingDeleteStock_andIsDeleted_thenReturnFalse() {
        UUID id = UUID.randomUUID();
        STOCK_ENTITY.setDeleted(true);
        when(stockRepo.findByStockId(id)).thenReturn(Optional.of(STOCK_DETAILS));
        when(mapper.toEntity(STOCK_DETAILS)).thenReturn(STOCK_ENTITY);

        boolean result = stockService.deleteStock(id);
        verify(stockRepo, never()).save(any());
        assertFalse(result);
        assertTrue(STOCK_ENTITY.isDeleted());

    }

    @Test
    void givenInvalidId_whenCallingDeleteStock_thenReturnFalse() {
        when(stockRepo.findByStockId(null)).thenReturn(Optional.empty());
        boolean result = stockService.deleteStock(null);
        verify(mapper, never()).toEntity(STOCK_DETAILS);
        verify(stockRepo, never()).save(any());
        assertFalse(result);
    }

    @Test
    void givenValidInput_whenCallingUpdateStock_thenExpectValidResult() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.of(STOCK_DETAILS));
        when(mapper.toEntity(STOCK_DETAILS)).thenReturn(STOCK_ENTITY);
        stockService.updateStock(id, STOCK_DETAILS);
        verify(stockRepo, atMostOnce()).save(STOCK_ENTITY);
    }

    @Test
    void givenNullId_whenCallingUpdateStock_thenThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> stockService.updateStock(null, STOCK_DETAILS));
    }

    @Test
    void givenInvalidId_whenCallingUpdateStock_thenThrowException() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> stockService.updateStock(id, STOCK_DETAILS));
    }

    @Test
    void givenValidIdAndNullStockDetails_whenCallingUpdateStock_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.updateStock(UUID.randomUUID(), null));
    }

    @Disabled
    @Test
    void givenValidInput_whenCallingFindByPriceAboveAndDateBefore_thenExpectResult() {
        when(stockRepo.findAllStocks()).thenReturn(List.of(STOCK_ENTITY, STOCK_ENTITY));
        List<StockSummary> expectedSummaries = List.of(
                new StockSummary(STOCK_ENTITY.getName(), STOCK_ENTITY.getPrice(), STOCK_ENTITY.getStockType()),
                new StockSummary(STOCK_ENTITY.getName(), STOCK_ENTITY.getPrice(), STOCK_ENTITY.getStockType())
        );

        List<StockDetails> actualSummaries = stockService.findByPriceAboveAndDateBefore(BigDecimal.valueOf(10), LocalDateTime.now());

        assertEquals(expectedSummaries.size(), actualSummaries.size());
    }

    @Test
    void givenNullPrice_whenCallingFindByPriceAboveAndDateBefore_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.findByPriceAboveAndDateBefore(null, LocalDateTime.now()));
    }

    @Test
    void givenNullDate_whenCallingFindByPriceAboveAndDateBefore_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.findByPriceAboveAndDateBefore(BigDecimal.valueOf(10), null));
    }

    @Test
    void givenValidInput_whenCallingFindByMarketCapAbove_thenExpectResult() {
        when(stockRepo.findAllStocks()).thenReturn(List.of(STOCK_ENTITY));
        List<StockSummary> expectedSummaries = List.of(
                new StockSummary(STOCK_ENTITY.getName(), STOCK_ENTITY.getPrice(), STOCK_ENTITY.getStockType())
        );
        List<StockDetails> actualSummaries = stockService.findByMarketCapAbove(BigDecimal.valueOf(10));

        assertEquals(expectedSummaries.size(), actualSummaries.size());
    }

    @Test
    void givenNullInput_whenCallingFindByMarketCapAbove_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.findByMarketCapAbove(null));
    }

    @Test
    void givenValidInput_whenCallingUpdateMarketCap_thenExpectResult() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.of(STOCK_DETAILS));
        when(mapper.toEntity(STOCK_DETAILS)).thenReturn(STOCK_ENTITY);
        stockService.updateMarketCap(id, BigDecimal.valueOf(11));
        verify(stockRepo, atMostOnce()).save(STOCK_ENTITY);
    }

    @Test
    void givenNullId_whenCallingUpdateMarketCap_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.updateMarketCap(null, BigDecimal.valueOf(11)));
    }

    @Test
    void givenNullMarketCap_whenCallingUpdateMarketCap_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.updateMarketCap(UUID.randomUUID(), null));
    }

    @Test
    void givenInvalidId_whenCallingUpdateMarketCap_thenThrowException() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> stockService.updateMarketCap(id, BigDecimal.valueOf(11)));
    }

    @Test
    void givenValidInput_whenCallingUpdateStockType_thenExpectResult() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.of(STOCK_DETAILS));
        when(mapper.toEntity(STOCK_DETAILS)).thenReturn(STOCK_ENTITY);
        stockService.updateStockType(id, StockTypes.COMMON);
        verify(stockRepo, atMostOnce()).save(STOCK_ENTITY);
    }

    @Test
    void givenNullId_whenCallingUpdateStockType_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.updateStockType(null, StockTypes.COMMON));
    }

    @Test
    void givenNullType_whenCallingUpdateStockType_thenThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> stockService.updateStockType(UUID.randomUUID(), null));
    }

    @Test
    void givenInvalidId_whenCallingUpdateStockType_thenThrowException() {
        UUID id = UUID.randomUUID();
        when(stockRepo.findByStockId(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> stockService.updateStockType(id, StockTypes.COMMON));
    }

    @Disabled
    @Test
    void givenValidInput_whenCallingDeleteByOwnerCount_andIsNoDeleted_thenExpectTrue() {
        when(stockRepo.findAllStocks()).thenReturn(List.of(STOCK_ENTITY));
        assertTrue(stockService.deleteByOwnerCount(10));
        verify(stockRepo, atLeastOnce()).save(STOCK_ENTITY);
    }

    @Disabled
    @Test
    void givenValidInput_whenCallingDeleteByOwnerCount_andListIsEmpty_thenExpectFalse() {
        when(stockRepo.findAllStocks()).thenReturn(List.of(STOCK_ENTITY));
        assertFalse(stockService.deleteByOwnerCount(5));
        verify(stockRepo, never()).save(STOCK_ENTITY);
    }
}