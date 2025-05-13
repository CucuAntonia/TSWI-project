package com.zegasoftware.stock_management.model.entity;

import com.zegasoftware.stock_management.model.enums.StockTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stocks")
@NoArgsConstructor @Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_owner_count")
    private int stockOwnerCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "stock_type", nullable = false)
    private StockTypes stockType;

    @Column(nullable = false, name = "market_cap")
    private BigDecimal marketCap;

    @CreatedDate
    @Column(name = "stock_creation_date")
    private LocalDateTime stockCreation;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
