package com.example.StockMarketCharting.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.example.StockMarketCharting.dao.IPODetailRepository;
import com.example.StockMarketCharting.model.Company;
import com.example.StockMarketCharting.model.IPODetail;



@CrossOrigin
@RestController
public class IPODetailController {
	
	@Autowired
	IPODetailRepository ipoRepo;
	
	@Autowired
	CompanyRepository compRepo;
	
	
	//ADD OR UPDATE IPO
	@RequestMapping(value = "/addOrUpdateIPO",method=RequestMethod.POST)
	public String addOrUpdateIPO(@RequestBody Map<String, String> ipoCreds){	
	
		LocalDateTime openDateTime;
		double pricePerShare;
		long totalNumberOfShares;
		String remarks, compCode, openDateTimeStr,
		message ;
		pricePerShare = Double.parseDouble(ipoCreds.get("pricePerShare"));
		totalNumberOfShares = Long.parseLong(ipoCreds.get("totalNumberOfShares"));
		remarks = ipoCreds.get("remarks");
		compCode = ipoCreds.get("compCode");
		openDateTimeStr = ipoCreds.get("openDateTime").replace('T', ' ');
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		openDateTime = LocalDateTime.parse(openDateTimeStr, formatter);
		IPODetail ipo = null; 
		

		Company comp = compRepo.findByCode(compCode);
		if (comp == null)
			message = "Wrong Company Code";
			
		else
		{
			
			if (comp.getIpo() == null)
				ipo = new IPODetail(pricePerShare,  totalNumberOfShares, openDateTime);
			
			else 
			{
				ipo = comp.getIpo();
				ipo.setPricePerShare(pricePerShare);
				ipo.setTotalNumberOfShares(totalNumberOfShares);
				ipo.setOpenDateTime(openDateTime);
			}
				
			ipo.setCompName(comp.getName());
			ipo.setCompCode(compCode);
			ipo.setRemarks(remarks);
			ipo.setCompany(comp);
			ipoRepo.save(ipo);
			
			comp.setIpo(ipo);
			compRepo.save(comp);
			
			
//			Company comp1 = compRepo.findByCode(compCode);
//			System.out.println(comp1.getIpo());
//			System.out.println("CHECK");
			
			message = "IPO Added / Changes Saved";
		}

		return message;

	}
	
	// 
	@RequestMapping(value = "/getCompanyIPODetails/{compCode}",method=RequestMethod.GET)
	public IPODetail getCompanyIPODetails(@PathVariable String compCode){	
	
		Company comp = compRepo.findByCode(compCode);
		return comp.getIpo();

	}
	
	
	// Get all IPOs
	@RequestMapping(value = "/getAllIPO",method=RequestMethod.GET)
	public List<IPODetail> getAllIPO(){	
	
		return ipoRepo.findByOrderByOpenDateTimeDesc();

	}

	
	
	
}
