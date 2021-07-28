package com.example.StockMarketCharting.controller;

import java.util.HashMap;
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
import com.example.StockMarketCharting.model.Company;
import com.example.StockMarketCharting.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@CrossOrigin
@RestController
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class CompanyController {
	
	@Autowired
	CompanyRepository compRep;
	
	// update Company
	@RequestMapping(value = "/updateCompany",method=RequestMethod.POST)
	public String updateCompany(@RequestBody Map<String, String> newCreds){	
	
		Double turnover;
		String sectorName, code, name, ceo, boardOfDirectors, companyBrief, 
		message ;
		sectorName = newCreds.get("sectorName");
		code = newCreds.get("code");
		name = newCreds.get("name");
		ceo = newCreds.get("ceo");
		boardOfDirectors = newCreds.get("boardOfDirectors");
		companyBrief = newCreds.get("companyBrief");
		turnover = Double.parseDouble(newCreds.get("turnover"));
		
		
		
		Company comp = compRep.findByCode(code);
		if (comp == null)
			message = "Company not found";

		else
		{
			comp.setBoardOfDirectors(boardOfDirectors);
			comp.setCeo(ceo);
			comp.setCompanyBrief(companyBrief);
			comp.setName(name);
			comp.setTurnover(turnover);
			comp.setSectorName(sectorName);
			compRep.save(comp);
			message = "Company Records Updated";
		}
			
		
		return message;

	}
	
	// Deactivate Company
	@RequestMapping(value = "/deactivateCompany",method=RequestMethod.POST)
	public String deactivateCompany(@RequestBody Map<String, String> compCodeMap){	
	
		Double turnover;
		String code, message ;
		code = compCodeMap.get("code");
		Company comp = compRep.findByCode(code);		

		if (comp == null)
			message = "Company not found";

		else
		{
			comp.setDeactivate(true);
			compRep.save(comp);
			message = "Company has been deactivated";
		}
			
		
		return message;

	}
	
	
	
	// Get Company Details
		@RequestMapping(value = "/getCompany/{name}", method=RequestMethod.GET)
		public List<Company> getCompanyDetails(@PathVariable String name){	
		
			String message;
			List<Company> compList = compRep.findByName(name);		
			
			
			return compList;

		}
		
		// Get Company Details using code
		@RequestMapping(value = "/getCompanyByCode/{code}", method=RequestMethod.GET)
		public Company getCompanyByCode(@PathVariable String code){	
		
			String message;
			Company comp = compRep.findByCode(code);		
			
			
			return comp;

		}
		
		// Get All Companies
		@RequestMapping(value = "/getAllCompanies", method=RequestMethod.GET)
		public List<Company> getAllCompanies(){	
		
			String message;
			List<Company> compList = compRep.findAll();		
			
			
			return compList;

		}
		
		// Delete Company By Id
		@RequestMapping(value = "/deleteCompany", method=RequestMethod.POST)
		public String deleteCompany(@RequestBody Long id){	
		
			compRep.deleteById(id);
			return "Deleted";

		}
	
	
	

}
