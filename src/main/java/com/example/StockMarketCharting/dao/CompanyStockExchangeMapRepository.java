package com.example.StockMarketCharting.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import com.example.StockMarketCharting.model.CompanyStockExchangeMap;

@Component
public interface CompanyStockExchangeMapRepository extends JpaRepository<CompanyStockExchangeMap,Long>{

}
