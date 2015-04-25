package com.prediccion.acciones.service;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.prediccion.acciones.domain.Company;

@ContextConfiguration(locations = { "/META-INF/spring/applicationContext.xml" })
public class ParsingServiceTest extends AbstractJUnit4SpringContextTests{
	
    @Autowired
    ParsingService parsingService;

    @Test
    public void test(){
    	
    	String london_stock_market = "exchange%20%3D%3D%20%22LON%22%29%20%26%20%28";
    	String london_query = "https://www.google.com/finance?output=json&start=0&"
    			+ "num=20"
    			+ "&noIL=1&q=[%28"
    			+ london_stock_market
    			+ "market_cap%20%3E%3D%200%29%20%26%20%28"
    			+ "market_cap%20%3C%3D%20221450000000%29%20%26%20%28"
    			+ "pe_ratio%20%3E%3D%200%29%20%26%20%28"
    			+ "pe_ratio%20%3C%3D%20250667%29%20%26%20%28"
    			+ "dividend_yield%20%3E%3D%200%29%20%26%20%28"
    			+ "dividend_yield%20%3C%3D%20141%29%20%26%20%28"
    			+ "price_change_52week%20%3E%3D%20-98.33%29%20%26%20%28"
    			+ "price_change_52week%20%3C%3D%20981%29"
    			+ "]&restype=company&ei=0NA7VZHHLOuJsge4_YHQBw"
    			+"&sortas=Price52WeekPercChange";
    	
    	
		String paris_stock_market = "exchange%20%3D%3D%20%22EPA%22%29%20%26%20%28";
		String paris_price_change_52weeks = "-84";
    	
    	
		String paris_query = "https://www.google.com/finance?output=json&start=0&"
				+ "num=100"
				+ "&noIL=1&q=[%28"
				+ paris_stock_market
				+ "market_cap%20%3E%3D%200%29%20%26%20%28"
				+ "market_cap%20%3C%3D%20127180000000%29%20%26%20%28"
				+ "pe_ratio%20%3E%3D%200%29%20%26%20%28"
				+ "pe_ratio%20%3C%3D%2010098%29%20%26%20%28"
				+ "dividend_yield%20%3E%3D%200%29%20%26%20%28"
				+ "dividend_yield%20%3C%3D%20171%29%20%26%20%28"
				+ "price_change_52week%20%3E%3D%20"+paris_price_change_52weeks+"%29%20%26%20%28"
				+ "price_change_52week%20%3C%3D%201010%29]&restype=company&ei=jfs2VZHuH-zwsQfMhoCAAw&"+
				"sortas=Price52WeekPercChange";
				//"sortas=MarketCap";

		String maxNumEmpresas = "20";
		String price_change_52week_from = "-90";
		String marketCap_from = "1000000";
		String nasdaq = "exchange%20%3D%3D%20%22NASDAQ%22%29%29%20%26%20%28";
		String nysemkt = "exchange%20%3D%3D%20%22NYSEMKT%22%29%20%7C%20%28";
		String nyse = "exchange%20%3D%3D%20%22NYSE%22%29%20%7C%20%28"; 
		String tcbb = "exchange%20%3D%3D%20%22OTCBB%22%29%20%7C%20%28";
		String currency = "currency%20%3D%3D%20%22USD%22%20%26%20%28%28";
		String nyseArca = "exchange%20%3D%3D%20%22NYSEARCA%22%29%20%7C%20%28";
		String otcmkt = "exchange%20%3D%3D%20%22OTCMKTS%22%29%20%7C%20%28";
		
		String us_query = "http://www.google.com/finance?"+
						"output=json&start=0&num="+maxNumEmpresas+"&noIL=1&q=["+
						currency+
						otcmkt+
						tcbb+
						nysemkt+
						nyseArca+
						nyse+
						nasdaq+
						"market_cap%20%3E%3D%20"+marketCap_from+"%29%20%26%20%28"+
						"market_cap%20%3C%3D%20726640000000%29%20%26%20%28"+
						"pe_ratio%20%3E%3D%2010%29%20%26%20%28"+
						"pe_ratio%20%3C%3D%2078.57%29%20%26%20%28"+
						"dividend_yield%20%3E%3D%200%29%20%26%20%28"+
						"dividend_yield%20%3C%3D%201976%29%20%26%20%28"+
						"price_change_52week%20%3E%3D%20"+price_change_52week_from+"%29%20%26%20%28"+
						"price_change_52week%20%3C%3D%2019901%29]&"+
						"restype=company&"+
						"ei=9LIzVcHMJcmUsQeLtoDQDw"+
						"&sortas=Price52WeekPercChange";
						//"&sortas=MarketCap";
		
		
    	Set<Company> list = parsingService.getSocksFromGoogleFinance(paris_query);
    	Set<Company> list2 = parsingService.getSocksFromGoogleFinance(us_query);
    	Set<Company> list3 = parsingService.getSocksFromGoogleFinance(london_query);

    	list.addAll(list2);
    	list.addAll(list3);
    	
    	Integer count =0 ;
    	for (Company company : list) {
			System.out.println(++count +" "+company);
		}
    	
    }
    
}
