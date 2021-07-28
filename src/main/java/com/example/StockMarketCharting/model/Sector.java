package com.example.StockMarketCharting.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sector {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;

		@Column(nullable = false, unique = true)
		private String sectorName;

		@Column(nullable = false)
		private String brief;

		@OneToMany(mappedBy = "sector")
		private List<Company> companies = new ArrayList<>();

		protected Sector() {
			super();
		}
		

		public Sector(String sectorName, String brief) {
			super();
			this.sectorName = sectorName;
			this.brief = brief;


		}


		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public String getSectorName() {
			return sectorName;
		}


		public void setSectorName(String sectorName) {
			this.sectorName = sectorName;
		}


		public String getBrief() {
			return brief;
		}


		public void setBrief(String brief) {
			this.brief = brief;
		}


		public List<Company> getCompanies() {
			return companies;
		}


		public void setCompanies(List<Company> companies) {
			this.companies = companies;
		}


	
	

}
