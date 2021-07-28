package com.example.StockMarketCharting.model;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NamedQuery(name = "Company.findByname", query = "SELECT c FROM Company c WHERE c.name = :name")
@Entity
@Table(name = "Company")
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private boolean deactivate; 


	public boolean isDeactivate() {
		return deactivate;
	}

	public void setDeactivate(boolean deactivate) {
		this.deactivate = deactivate;
	}

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false)
	private Double turnover;

	@Column(nullable = false)
	private String ceo;

	@Column(nullable = false)
	@Type(type = "text")
	private String boardOfDirectors;
	
	@Column(nullable = false)
	@Type(type = "text")
	private String companyBrief;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private IPODetail ipo;

	@OneToMany(targetEntity = CompanyStockExchangeMap.class)
	@JsonIgnore
	private List<CompanyStockExchangeMap> compstockmap;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Sector sector;
	
	@Column(nullable = false)
	private String sectorName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	protected Company() {
		super();
	}

	public Company(String companyName, double turnover, String ceo, String boardOfDirectors, String companyBrief) {
		super();
		this.name = companyName;
		this.turnover = turnover;
		this.ceo = ceo;
		this.boardOfDirectors = boardOfDirectors;
		this.companyBrief = companyBrief;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String companyName) {
		this.name = companyName;
	}

	public Double getTurnover() {
		return turnover;
	}

	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getBoardOfDirectors() {
		return boardOfDirectors;
	}

	public void setBoardOfDirectors(String boardOfDirectors) {
		this.boardOfDirectors = boardOfDirectors;
	}

	public String getCompanyBrief() {
		return companyBrief;
	}

	public void setCompanyBrief(String companyBrief) {
		this.companyBrief = companyBrief;
	}

	public IPODetail getIpo() {
		return ipo;
	}

	public void setIpo(IPODetail ipo) {
		this.ipo = ipo;
	}

	public List<CompanyStockExchangeMap> getCompstockmap() {
		return compstockmap;
	}

	public void setCompstockmap(List<CompanyStockExchangeMap> compstockmap) {
		this.compstockmap = compstockmap;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}


	

}
