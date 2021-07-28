package com.example.StockMarketCharting.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StockMarketCharting.model.Sector;
import org.springframework.stereotype.Component;

@Component
public interface SectorRepository extends JpaRepository<Sector,Long>{

	Sector findBySectorName(String sectorName);

}
