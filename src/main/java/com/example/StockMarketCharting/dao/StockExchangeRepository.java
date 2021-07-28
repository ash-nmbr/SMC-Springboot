package com.example.StockMarketCharting.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StockMarketCharting.model.StockExchange;
import org.springframework.stereotype.Component;

@Component
public interface StockExchangeRepository extends JpaRepository<StockExchange,Long>{

	List<StockExchange> findByName(String string);

}
