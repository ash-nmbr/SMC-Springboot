package com.example.StockMarketCharting.controller;

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
import com.example.StockMarketCharting.dao.SectorRepository;
import com.example.StockMarketCharting.model.Company;
import com.example.StockMarketCharting.model.Sector;
import com.example.StockMarketCharting.model.StockPrice;



@CrossOrigin
@RestController
public class SectorController {
	
	@Autowired
	CompanyRepository compRep;
	
	@Autowired
	SectorRepository sectRep;
	
	@RequestMapping(value = "/addSector",method=RequestMethod.POST)
	public String addSector(@RequestBody Sector sector){	
	
		
		String sectorName;
		sectorName = sector.getSectorName();
		List<Company> compList = compRep.findBySectorName(sectorName);
		
		sector.getCompanies().addAll(compList);

		sectRep.save(sector);
		
		for(Company c: compList)
		{
			c.setSector(sector);
			compRep.save(c);
		}
		
		return "Sector added";

	}
	
	
	@RequestMapping(value = "/getCompaniesOfSector/{sectorName}",method=RequestMethod.GET)
	public List<Company> getCompaniesOfSector(@PathVariable String sectorName){	
		
		Sector sct = sectRep.findBySectorName(sectorName);
		return sct.getCompanies();

	}
	
	@RequestMapping(value = "/getAllSectors",method=RequestMethod.GET)
	public List<Sector> getAllSectors(){	

		return sectRep.findAll();

	}
	
	@RequestMapping(value = "/getSectorByName/{sectorName}",method=RequestMethod.GET)
	public Sector getSectorByName(@PathVariable String sectorName){	

		return sectRep.findBySectorName(sectorName);

	}
	
	@RequestMapping(value = "/updateSector",method=RequestMethod.POST)
	public String updateSector(@RequestBody Map<String, String> text){	
		
		Sector sct = sectRep.findBySectorName(text.get("sectorName"));
		sct.setBrief(text.get("brief"));
		sectRep.save(sct);
		return "Sector Updated Successfully";

	}
	
	

}
