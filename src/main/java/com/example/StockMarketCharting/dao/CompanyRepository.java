package com.example.StockMarketCharting.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.example.StockMarketCharting.model.Company;

@Component 
public interface CompanyRepository extends JpaRepository<Company,Long>{

	Company findByCode(String code);

	List<Company> findBySectorName(String sectorName);

	List<Company> findByName(String name);

}
