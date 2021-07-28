package com.example.StockMarketCharting.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.StockMarketCharting.model.IPODetail;
import org.springframework.stereotype.Component;

@Component
public interface IPODetailRepository extends JpaRepository<IPODetail,Long>{

	List<IPODetail> findByOrderByOpenDateTimeDesc();

}
