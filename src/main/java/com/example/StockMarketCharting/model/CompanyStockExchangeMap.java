package com.example.StockMarketCharting.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "CompanyStockExchangeMap")
public class CompanyStockExchangeMap {
	
	
	public CompanyStockExchangeMap() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue
	private long id;
	private String CompanyCode;
	@ManyToOne(fetch = FetchType.EAGER)
	private Company company;
	@ManyToOne(fetch = FetchType.LAZY)
	private StockExchange stockexchange;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCompanyCode() {
		return CompanyCode;
	}
	public void setCompanyCode(String companyCode) {
		CompanyCode = companyCode;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public StockExchange getStockexchange() {
		return stockexchange;
	}
	public void setStockexchange(StockExchange stockexchange) {
		this.stockexchange = stockexchange;
	}





	

}
