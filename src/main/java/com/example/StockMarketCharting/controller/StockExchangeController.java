package com.example.StockMarketCharting.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.StockMarketCharting.dao.CompanyRepository;
import com.example.StockMarketCharting.dao.CompanyStockExchangeMapRepository;
import com.example.StockMarketCharting.dao.SectorRepository;
import com.example.StockMarketCharting.dao.StockExchangeRepository;
import com.example.StockMarketCharting.model.Company;
import com.example.StockMarketCharting.model.CompanyStockExchangeMap;
import com.example.StockMarketCharting.model.Sector;
import com.example.StockMarketCharting.model.StockExchange;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

@CrossOrigin
@RestController
public class StockExchangeController {

	
	@Autowired
	CompanyRepository compRep;
	
	@Autowired
	SectorRepository sectRep;
	
	@Autowired
	StockExchangeRepository stkExRep;
	
	@Autowired
	CompanyStockExchangeMapRepository csemRep;
	
	
	
	@RequestMapping(value = "/getStockExchangesList",method=RequestMethod.GET)
	public List<StockExchange> getStockExchangesList(){	

		return stkExRep.findAll();

	}
	
	@RequestMapping(value="/addStockExchange",method=RequestMethod.POST)
	public StockExchange addStockExchange(@RequestBody StockExchange exch)  {

		stkExRep.save(exch);
		return exch;

	}
	
	@RequestMapping(value = "/getCompaniesList/{stockExchangeName}",method=RequestMethod.GET)
	public List<Company> getCompaniesList(@PathVariable String stockExchangeName){	
		

		List<CompanyStockExchangeMap> cseList = csemRep.findAll();

		List<Company> compList = new ArrayList<Company>();
		for (CompanyStockExchangeMap cse: cseList)
		{
			
			if ( cse.getStockexchange().getName().equals(stockExchangeName) )
			{
				System.out.println("check");
				System.out.println(cse.getCompany().getCode());
				compList.add(cse.getCompany());

			}
				
		}
		
		for(Company c: compList)
		{
			System.out.println(c.getCode());
		}
		
		return compList;

	}
	
	
	@RequestMapping(value = "/getExchangeByName/{stockExchangeName}",method=RequestMethod.GET)
	public List<StockExchange> getExchangeByName(@PathVariable String stockExchangeName){	
		
		return stkExRep.findByName(stockExchangeName);
		

	}
	
	@RequestMapping(value="/updateExchange",method=RequestMethod.POST)
	public String updateExchange(@RequestBody Map<String, String> text)  {

		String name, brief, contactAddress;
		name = text.get("name");
		brief = text.get("brief");
		contactAddress = text.get("contactAddress");
		StockExchange stckex = stkExRep.findByName(name).get(0);
		stckex.setBrief(brief);
		stckex.setContactAddress(contactAddress);
		stkExRep.save(stckex);
		
		return name;

	}
	
	
	
}
