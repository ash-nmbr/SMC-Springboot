package com.example.StockMarketCharting.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StockMarketCharting.model.StockPrice;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface StockPriceRepository extends JpaRepository<StockPrice,Long> {

	List<StockPrice> findByCompanycode(String compCode);

	List<StockPrice> findByExchangename(String exchangename);


}
