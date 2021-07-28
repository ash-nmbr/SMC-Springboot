package com.example.StockMarketCharting.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.StockMarketCharting.dao.CompanyRepository;
import com.example.StockMarketCharting.dao.CompanyStockExchangeMapRepository;
import com.example.StockMarketCharting.dao.SectorRepository;
import com.example.StockMarketCharting.dao.StockExchangeRepository;
import com.example.StockMarketCharting.model.Company;
import com.example.StockMarketCharting.model.CompanyStockExchangeMap;
import com.example.StockMarketCharting.model.Sector;
import com.example.StockMarketCharting.model.StockExchange;



@CrossOrigin
@RestController
public class Maincontroller {
	
	
	@Autowired
	CompanyRepository cmprep;
	@Autowired
	StockExchangeRepository stkrep;
	@Autowired
	CompanyStockExchangeMapRepository stkcmpmaprep;
	@Autowired
	EntityManager em;
	@Autowired
	SectorRepository sectRep;
	
	// add company
	@RequestMapping(value = "/company", method = RequestMethod.POST)
	public Company createcompany(@RequestBody Company cmp) {

		String sectorName;
		sectorName = cmp.getSectorName();
		Sector sct = sectRep.findBySectorName(sectorName);
		if (sct != null)
		{
			cmp.setSector(sct);
			sct.getCompanies().add(cmp);
			sectRep.save(sct);
		}
		cmprep.save(cmp);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cmp.getId())
				.toUri();

		return cmp;
	}

	@RequestMapping(value = "/mapcompanycode", method = RequestMethod.POST)
	// pass map of string in requestbody ,instead of pojo class to get
	// non entity based params
	public String mapcode(@RequestBody Map<String, String> text) {
		System.out.println("params100" + text.get("companyname"));
		Query query = em.createNamedQuery("Company.findByname");
		query.setParameter("name", text.get("companyname"));
		Company c = (Company) query.getSingleResult();

		StockExchange e = stkrep.findByName(text.get("exchangename")).get(0);
		CompanyStockExchangeMap cse = new CompanyStockExchangeMap();
		cse.setCompany(c);
		cse.setStockexchange(e);
		stkcmpmaprep.save(cse);
		return "Test";
		// return companyname;
	}
	
	

	@RequestMapping(value = "/listall", method = RequestMethod.GET)	
	public String listit() {
		
		String x = "";
		List<CompanyStockExchangeMap> csem = stkcmpmaprep.findAll();
		for (CompanyStockExchangeMap c:csem)  {
		Optional<StockExchange> s =	stkrep.findById(c.getStockexchange().getId()); 
		Optional<Company> cc =cmprep.findById(c.getCompany().getId());
			x= x + "   "+cc.get().getName() + "   "+s.get().getName();
			
			
		}
			
		return x;
		// return companyname;
	}
	
	
	@RequestMapping(value = "/getCompaniesInExchange/{exchangename}", method = RequestMethod.GET)	
	public List<String> getCompaniesInExchange(@PathVariable String exchangename) {
		
		String x = "";
		List<String> companies = new ArrayList<String>();
		List<CompanyStockExchangeMap> csem = stkcmpmaprep.findAll();
		for (CompanyStockExchangeMap c:csem)  {
		Optional<StockExchange> s =	stkrep.findById(c.getStockexchange().getId()); 
		Optional<Company> cc =cmprep.findById(c.getCompany().getId());
			x= x + "   "+cc.get().getName() + "   "+s.get().getName();
			
			if ( s.get().getName().equals(exchangename) )
			{
				companies.add(cc.get().getName());
			}
		}
			
		return companies;
		// return companyname;
	}
	

}
