package com.example.StockMarketCharting.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.StockMarketCharting.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public interface UserModelRepository extends JpaRepository<UserModel,Long>{

	UserModel findByName(String name);

	UserModel findByEmail(String email);

}
