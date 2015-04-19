package com.prediccion.acciones.service;

import java.net.URLEncoder;

import com.prediccion.acciones.utils.HttpConectionUtils;

public class ParsingServiceImpl {

	
	public void loviejo(){
		/*
		String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
		
		final String financialTimes = "http://markets.ft.com/research/Markets/Tearsheets/Forecasts?s="+this.equity.symbol+":"+this.equity.market;
		final String yql ="select * from html where url='"+financialTimes+"' "+"and xpath='//table[@class=\"fright\"]/tbody/tr/td[2]/span'"; 
		
		final String fullUrlStr = baseUrl + URLEncoder.encode(yql, "UTF-8") + "&format=json";
		*/
	}
	
	public void getSocksFromGoogleFinance(){
		
		
		String query = "http://www.google.com/finance?"+
						"output=json&start=0&num=2000&noIL=1&q=["+
						"currency%20%3D%3D%20%22USD%22%20%26%20%28%28"+
						"exchange%20%3D%3D%20%22OTCMKTS%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22OTCBB%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22NYSEMKT%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22NYSEARCA%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22NYSE%22%29%20%7C%20%28"+
						"exchange%20%3D%3D%20%22NASDAQ%22%29%29%20%26%20%28"+
						"market_cap%20%3E%3D%20152870000%29%20%26%20%28"+
						"market_cap%20%3C%3D%20726640000000%29%20%26%20%28"+
						"pe_ratio%20%3E%3D%200%29%20%26%20%28"+
						"pe_ratio%20%3C%3D%2078.57%29%20%26%20%28"+
						"dividend_yield%20%3E%3D%200%29%20%26%20%28"+
						"dividend_yield%20%3C%3D%201976%29%20%26%20%28"+
						"price_change_52week%20%3E%3D%20106%29%20%26%20%28"+
						"price_change_52week%20%3C%3D%2019901%29]&"+
						"restype=company&"+
						"ei=9LIzVcHMJcmUsQeLtoDQDw"+
						"&sortas=MarketCap";
		
		String result="";
		try {
			result = HttpConectionUtils.getData(query);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
