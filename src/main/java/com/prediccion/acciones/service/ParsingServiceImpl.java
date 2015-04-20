package com.prediccion.acciones.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.prediccion.acciones.domain.Company;
import com.prediccion.acciones.domain.CompanyProperty;
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
	
	
	public List<Company> createCompanies(CompanyJson[] companyArray){
		List<Company> list = new ArrayList<Company>();
		
		Company company = null;
		for (CompanyJson c : companyArray) {
			company = new Company(c.title, c.ticker, c.exchange, c.id, c.local_currency_symbol, new ArrayList<CompanyProperty>());
			CompanyProperty property = null;
			for (ColumnJson col : c.columns) {
				property = new CompanyProperty();

				try{
					if(StringUtils.isEmpty(col.field)||StringUtils.isEmpty(col.value)){
						throw new Exception();
					}else{
						property.setDisplayName(col.display_name);
						property.setField(col.field);
						
						if(col.value.contains("B")){
							int i = col.value.indexOf("B");
							String sub1 = col.value.substring(0,i);
							Double val = Double.valueOf(sub1)*1000000000D;
							col.value = val.toString();
						}
						property.setPropertyValue(Double.valueOf(col.value));
						property.setPropertyOrder(col.sort_order);
					}
					company.getProperties().add(property);
					
				}catch(NumberFormatException e){
					System.out.println("numberFormatException property skipped");
				}catch(Exception e){
					System.out.println("field or value empty property skipped");
				}
			}
			list.add(company);
		}
		return list;
	}
	
	public List<Company> getSocksFromGoogleFinance(){
		
		List<Company> companyList = null;
		String maxNumEmpresas = "8000";
		String price_change_52week_from = "-50";
		String marketCap_from = "1000000";
		String nasdaq = "exchange%20%3D%3D%20%22NASDAQ%22%29%29%20%26%20%28";
		String nysemkt = "exchange%20%3D%3D%20%22NYSEMKT%22%29%20%7C%20%28";
		String nyse = "exchange%20%3D%3D%20%22NYSE%22%29%20%7C%20%28"; 
		String tcbb = "exchange%20%3D%3D%20%22OTCBB%22%29%20%7C%20%28";
		String currency = "currency%20%3D%3D%20%22USD%22%20%26%20%28%28";
		String nyseArca = "exchange%20%3D%3D%20%22NYSEARCA%22%29%20%7C%20%28";
		String london = "%28exchange%20%3D%3D%20%22LON%22%29%20%26%20%28";
		String otcmkt = "exchange%20%3D%3D%20%22OTCMKTS%22%29%20%7C%20%28";
		
		String query = "http://www.google.com/finance?"+
						"output=json&start=0&num="+maxNumEmpresas+"&noIL=1&q=["+
						//currency+
						london+
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
						"&sortas=MarketCap";
		
		String result="";

		try {
			
			Pattern p = Pattern.compile("\\[\\{(\"title\")+[\\x00-\\x7F]+(?=\\,\\\"mf_searchresults)");
			Pattern p_european = Pattern.compile("\\[\\{\"title\"+[\\x00-\\x7F|€|£]+(?=\\,\\\"mf_searchresults)");
			
			result = HttpConectionUtils.getData(query);
			System.out.println(result);
			Matcher m = p_european.matcher(result);

			//\[\{("title")+[\x00-\x7F]+(?=\,\"mf_searchresults)
			CompanyJson[] companyArray = null;
			if(m.find()){
				String sub = result.substring(m.start(),m.end());
				System.out.println(sub);
				Gson gson = new Gson();
				companyArray = gson.fromJson(sub, CompanyJson[].class);			
				System.out.println(sub);
			}else{
				System.out.println("no encontro "+"\\[\\{(\"title\")+[\\x00-\\x7F]+(?=\\,\\\"mf_searchresults)");
			}
			
		    companyList = createCompanies(companyArray);
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return companyList;
	}
	
	
	
	
	
}
class Equity implements Comparable<Equity>{
	String symbol;
	String market;
	String description;
	Double maxForecastValue;
	Double medForecastValue;
	Double minForecastValue;
	
	public Equity(String symbol,String market){
		this.symbol=symbol;
		this.market=market;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((market == null) ? 0 : market.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equity other = (Equity) obj;
		if (market == null) {
			if (other.market != null)
				return false;
		} else if (!market.equals(other.market))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@Override
	public int compareTo(Equity o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "Equity [symbol=" + symbol + ", market=" + market
				+ ", description=" + description + ", maxForecastValue="
				+ maxForecastValue + ", medForecastValue=" + medForecastValue
				+ ", minForecastValue=" + minForecastValue + "]";
	}
	
	
}

class CompanyJson {
	
	String title;
	String id;
	String is_active;
	String ticker;
	String exchange;
	String is_supported_exchange;
	String local_currency_symbol;
	ArrayList<ColumnJson> columns;
	
}
class ColumnJson{
	String display_name;
	String value;
	String field;
	String sort_order;
}
