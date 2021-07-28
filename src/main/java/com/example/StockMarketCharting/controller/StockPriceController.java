package com.example.StockMarketCharting.controller;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.sql.Date;
import java.util.List;

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
import com.example.StockMarketCharting.dao.StockPriceRepository;
import com.example.StockMarketCharting.model.Company;
import com.example.StockMarketCharting.model.CompanyStockExchangeMap;
import com.example.StockMarketCharting.model.Sector;
import com.example.StockMarketCharting.model.StockExchange;
import com.example.StockMarketCharting.model.StockPrice;



@CrossOrigin
@RestController
public class StockPriceController {
	
	@Autowired
	CompanyRepository compRep;
	
	@Autowired
	StockPriceRepository spRep;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	SectorRepository sectRep;
	
	@Autowired
	StockExchangeRepository stkrep;
	
	@Autowired
	CompanyStockExchangeMapRepository stkcmpmaprep;
	
	// Add Stock Price
	@RequestMapping(value = "/addStockPrice",method=RequestMethod.POST)
	public Map<String, String> addStockPrice(@RequestBody ArrayList<ArrayList<String>> excel) throws ParseException{	
	
		Map<String, String> ret_map = new HashMap<String, String>();
		String companycode, exchangename, datestr, timestr, shareprice,  message ;
		message = "hi";
		Date datee;
		Time timee;
		
		for (int i = 1; i < excel.size(); i++)
		{

			companycode = excel.get(i).get(0);
			exchangename = excel.get(i).get(1);
			shareprice = excel.get(i).get(2);
			datestr = excel.get(i).get(3);
			timestr = excel.get(i).get(4);
			System.out.println(companycode);

			datee = Date.valueOf(datestr.trim().replace('/', '-'));
			timee = Time.valueOf(timestr.trim());
			System.out.println(datee);
			
			if (i == 1)
			{
				
				String companyname;
				companyname = compRep.findByCode(companycode).getName();
	
				ret_map.put("companyname", companyname);
				ret_map.put("exchangename", exchangename);
				ret_map.put("fromdate", datestr);
				
				
//				remove if doesnt work....this is for company and exchange mapping
//				System.out.println("params100" + text.get("companyname"));
				Query query = em.createNamedQuery("Company.findByname");
				query.setParameter("name", companyname);
				Company c = (Company) query.getSingleResult();

				StockExchange e = stkrep.findByName(exchangename).get(0);
				CompanyStockExchangeMap cse = new CompanyStockExchangeMap();
				cse.setCompany(c);
				cse.setStockexchange(e);
				stkcmpmaprep.save(cse);
				
			}
			
			if (i == excel.size()-1)
			{
				ret_map.put("todate", datestr);
			}
			
			StockPrice stkpr = new StockPrice();
			stkpr.setCompanycode(companycode.trim());
			stkpr.setExchangename(exchangename.trim());
			stkpr.setShareprice(Float.parseFloat(shareprice));
			stkpr.setDatee((java.sql.Date) datee);
			stkpr.setTimee(timee);
			
			spRep.save(stkpr);
			
		}
		
		ret_map.put("records", ""+excel.size());
		
		return ret_map;

	}
	
	// get stock price details of a company wrt
	// exchangename, company code and to and from date
	@RequestMapping(value = "/getCompanyStockPrice",method=RequestMethod.POST)
	public List<Map<String, String>> getCompanyStockPrice(@RequestBody Map<String, String> text) throws ParseException{	
		
		List<StockPrice> stkprList;
		String compCode, from, to, exchangename;
		Date fromDate, toDate;
		compCode = text.get("name");
		from = text.get("from");
		to = text.get("to");
		exchangename = text.get("exchangename");
		String arrfrom[] = from.split("-");
		String arrto[]  = to.split("-");

		System.out.println(from);
		System.out.println("hello");
		
		fromDate = Date.valueOf(from);
		toDate = Date.valueOf(to);
		
		System.out.println(fromDate);
		stkprList = spRep.findAll();	
		
		
		
		List<StockPrice> filterList = new ArrayList<StockPrice>();
		
		float avg, total, min, max, growth, minDatePrice, maxDatePrice;
		avg = 0;
		total = 0;
		min = Float.MAX_VALUE;
		max = Float.MIN_VALUE;
		growth = 0;
		minDatePrice = 0;
		maxDatePrice = 0;
		List<Float> prices = new ArrayList<Float>();
		
		System.out.println();
		System.out.println(exchangename+compCode);
		for (StockPrice p: stkprList)
		{
			if ( (p.getDatee().before(toDate) || p.getDatee().equals(toDate))&&
					(p.getDatee().after(fromDate) || p.getDatee().equals(fromDate))
					&& p.getExchangename().trim().equals(exchangename)
					&& p.getCompanycode().trim().equals(compCode)
					)
			{
				total += p.getShareprice();
				filterList.add(p);
				if (min > p.getShareprice())
					min = p.getShareprice();
				
				if (max < p.getShareprice())
					max = p.getShareprice();

			}
			
			if (p.getDatee().equals(fromDate))
				minDatePrice = p.getShareprice();
			
			if (p.getDatee().equals(toDate))
				maxDatePrice = p.getShareprice();
			
		}
		growth = maxDatePrice - minDatePrice;
		avg = total/filterList.size();
		System.out.println(min);
		System.out.println(max);
		System.out.println(avg);
		System.out.println(growth);
		System.out.println(total);
//		System.out.println(min);
		
		List<Map<String, String>> ret_val= new ArrayList<Map<String, String>>();
		Map<String, String> hm1 = new HashMap<String, String>();
		hm1.put("label", "Minimum Stock Price");
		hm1.put("value", min+"");
		ret_val.add(hm1);
		
		Map<String, String> hm2 = new HashMap<String, String>();
		hm2.put("label", "Maximum Stock Price");
		hm2.put("value", max+"");
		ret_val.add(hm2);
		
		Map<String, String> hm3 = new HashMap<String, String>();
		hm3.put("label", "Average Stock Price");
		hm3.put("value", avg+"");
		ret_val.add(hm3);
		
		Map<String, String> hm4 = new HashMap<String, String>();
		hm4.put("label", "Growth");
		hm4.put("value", growth+"");
		ret_val.add(hm4);
			
		return ret_val;


	}
	
	// get stock price details of a sector wrt
	// exchangename, company code and to and from date
	@RequestMapping(value = "/getSectorStockPrice",method=RequestMethod.POST)
	public List<Map<String, String>> getSectorStockPrice(@RequestBody Map<String, String> text) throws ParseException{	
		
		List<StockPrice> stkprList;
		String sectorname, from, to, exchangename;
		Date fromDate, toDate;
		sectorname = text.get("name");
		from = text.get("from");
		to = text.get("to");
		exchangename = text.get("exchangename");
		String arrfrom[] = from.split("-");
		String arrto[]  = to.split("-");
		System.out.println(from);

		List<Company> compList = compRep.findBySectorName(sectorname);
		fromDate = Date.valueOf(from);
		toDate = Date.valueOf(to);
		
		List<StockPrice> filterList = new ArrayList<StockPrice>();
		stkprList = spRep.findAll();
		
		float avg, total, min, max, growth, minDatePrice, maxDatePrice;
		avg = 0;
		total = 0;
		min = Float.MAX_VALUE;
		max = Float.MIN_VALUE;
		growth = 0;
		minDatePrice = 0;
		maxDatePrice = 0;
		
		System.out.println();
		System.out.println(exchangename+sectorname);
		
		for (Company c: compList)
		{
			
			for (StockPrice p: stkprList)
			{
				System.out.println(p.getDatee().before(toDate));
				
				if ( (p.getDatee().before(toDate) || p.getDatee().equals(toDate))&&
						(p.getDatee().after(fromDate) || p.getDatee().equals(fromDate))
						&& p.getExchangename().trim().equals(exchangename)
						&& p.getCompanycode().trim().equals(c.getCode())
						)
				{
					total += p.getShareprice();
					filterList.add(p);
					if (min > p.getShareprice())
						min = p.getShareprice();
					
					if (max < p.getShareprice())
						max = p.getShareprice();

				}
				
				if (p.getDatee().equals(fromDate))
					minDatePrice = p.getShareprice();
				
				if (p.getDatee().equals(toDate))
					maxDatePrice = p.getShareprice();
				
			}
			
			
		}
		
		growth = maxDatePrice - minDatePrice;
		avg = total/filterList.size();
		System.out.println(min);
		System.out.println(max);
		System.out.println(avg);
		System.out.println(growth);
		System.out.println(total);
//		System.out.println(min);
		
		List<Map<String, String>> ret_val= new ArrayList<Map<String, String>>();
		Map<String, String> hm1 = new HashMap<String, String>();
		hm1.put("label", "Minimum Stock Price");
		hm1.put("value", min+"");
		ret_val.add(hm1);
		
		Map<String, String> hm2 = new HashMap<String, String>();
		hm2.put("label", "Maximum Stock Price");
		hm2.put("value", max+"");
		ret_val.add(hm2);
		
		Map<String, String> hm3 = new HashMap<String, String>();
		hm3.put("label", "Average Stock Price");
		hm3.put("value", avg+"");
		ret_val.add(hm3);
		
		Map<String, String> hm4 = new HashMap<String, String>();
		hm4.put("label", "Growth");
		hm4.put("value", growth+"");
		ret_val.add(hm4);

		return ret_val;


	}
	
	// get company stock price 
	@RequestMapping(value = "/getCompanyStockPriceDetails/{compCode}",method=RequestMethod.GET)
	public List<StockPrice> getCompanyStockPriceDetails(@PathVariable String compCode){	
	
		List<StockPrice> stockPriceList = new ArrayList<StockPrice>();
		Company comp = compRep.findByCode(compCode);

		if (comp == null)
			System.out.println("Invalid Company code");

		else
		{
//			Long id;
//			id = comp.getId();
			stockPriceList = spRep.findByCompanycode(compCode);
			System.out.println("Stock Price added");
		}
			
		
		return stockPriceList;

	}
	
	// get all stock prices
	//@CrossOrigin(origins ="http://localhost:3000")
	@RequestMapping(value = "/getAllStockPrices",method=RequestMethod.GET, headers = "Accept=application/json"  )
	public List<StockPrice> getAllStockPrices() throws ClassNotFoundException, IOException {
		 
	    List<StockPrice> stkprice= spRep.findAll();
	   // make sure your entity class properties of user are in lower case and match the json,to avoid errors
	    return stkprice;
	}
	
	

}











