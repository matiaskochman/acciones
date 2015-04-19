package com.prediccion.acciones.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prediccion.acciones.utils.HttpConectionUtils;

@Service
@Transactional
public class ParsingServiceImpl implements ParsingService{

	
	public void loviejo(){
		/*
		String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
		
		final String financialTimes = "http://markets.ft.com/research/Markets/Tearsheets/Forecasts?s="+this.equity.symbol+":"+this.equity.market;
		final String yql ="select * from html where url='"+financialTimes+"' "+"and xpath='//table[@class=\"fright\"]/tbody/tr/td[2]/span'"; 
		
		final String fullUrlStr = baseUrl + URLEncoder.encode(yql, "UTF-8") + "&format=json";
		*/
	}
	
	public void getSocksFromGoogleFinance(){
		
		
		String maxNumEmpresas = "1000";
		String ratio_from = "10";
		String marketCap_from = "1000000000";
		String nasdaq = "exchange%20%3D%3D%20%22NASDAQ%22%29%29%20%26%20%28";
		String nyse = "exchange%20%3D%3D%20%22NYSEMKT%22%29%20%7C%20%28";
		
		String query = "http://www.google.com/finance?"+
						"output=json&start=0&num="+maxNumEmpresas+"&noIL=1&q=["+
						"currency%20%3D%3D%20%22USD%22%20%26%20%28%28"+
						"exchange%20%3D%3D%20%22OTCMKTS%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22OTCBB%22%29%20%7C%20%28"+
						nyse+
						"exchange%20%3D%3D%20%22NYSEARCA%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22NYSE%22%29%20%7C%20%28"+
						nasdaq+
						"market_cap%20%3E%3D%20"+marketCap_from+"%29%20%26%20%28"+
						"market_cap%20%3C%3D%20726640000000%29%20%26%20%28"+
						"pe_ratio%20%3E%3D%2010%29%20%26%20%28"+
						"pe_ratio%20%3C%3D%2078.57%29%20%26%20%28"+
						"dividend_yield%20%3E%3D%200%29%20%26%20%28"+
						"dividend_yield%20%3C%3D%201976%29%20%26%20%28"+
						"price_change_52week%20%3E%3D%20"+ratio_from+"%29%20%26%20%28"+
						"price_change_52week%20%3C%3D%2019901%29]&"+
						"restype=company&"+
						"ei=9LIzVcHMJcmUsQeLtoDQDw"+
						"&sortas=MarketCap";
		
		String result="";
		
		try {
			result = HttpConectionUtils.getData(query);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
