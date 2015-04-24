package com.prediccion.acciones.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.prediccion.acciones.domain.Company;
import com.prediccion.acciones.process.Processor;
import com.prediccion.acciones.utils.HttpConectionUtils;

@Service
@Transactional
public class ParsingServiceImpl implements ParsingService{

	int CONCURRENT_THREADS = 50;
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
			//company = new Company(c.title, c.ticker, c.exchange, c.id, c.local_currency_symbol, new ArrayList<CompanyProperty>());
			
			company = new Company();
			
			company.setTitle(c.title);
			company.setTicker(c.ticker);
			company.setExchange(c.exchange);
			company.setCompanyId(c.id);
			company.setLocalCurrencySymbol(c.local_currency_symbol);
			
			//CompanyProperty property = null;
			for (ColumnJson col : c.columns) {
				//property = new CompanyProperty();

				try{
					if(StringUtils.isEmpty(col.field)||StringUtils.isEmpty(col.value)){
						throw new Exception();
					}else{
						
						if(col.field.equalsIgnoreCase("MarketCap")){
							if(col.value.contains("B")){
								int i = col.value.indexOf("B");
								String sub1 = col.value.substring(0,i);
								Double val = Double.valueOf(sub1)*1000000000D;
								col.value = val.toString();
							}
							company.setMarketCap(Double.valueOf(col.value));
						}else if(col.field.equalsIgnoreCase("PE")){
							company.setPe(Double.valueOf(col.value));
							
						}else if(col.field.equalsIgnoreCase("Price52WeekPercChange")){
							company.setPrice52WeekPercChange(Double.valueOf(col.value));
							
						}
						/*
						property.setDisplayName(col.display_name);
						property.setField(col.field);
						
						property.setPropertyValue(Double.valueOf(col.value));
						property.setPropertyOrder(col.sort_order);
						*/
					}
					//company.getProperties().add(property);
					
				}catch(NumberFormatException e){
					System.out.println("numberFormatException property skipped");
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("field or value empty property skipped");
				}
			}
			list.add(company);
		}
		return list;
	}
	
	public Set<Company> getSocksFromGoogleFinance(String query){
		
		
		List<Company> companyList = null;
		
		String result="";
		Comparator<Company> minComparator = new Comparator<Company>(){
			@Override
			public int compare(Company o1, Company o2) {
				int min = o1.getMinForecastPercentageValue().compareTo(o2.getMinForecastPercentageValue());
				return min*(-1);
			}
		};		    
		TreeSet<Company> resultSet = new TreeSet<Company>(minComparator);

		try {
			
			Pattern p = Pattern.compile("\\[\\{(\"title\")+[\\x00-\\x7F]+(?=\\,\\\"mf_searchresults)");
			Pattern p_european = Pattern.compile("\\[\\{\"title\"+[\\x00-\\x7F|€|£]+(?=\\,\\\"mf_searchresults)");
			
			result = HttpConectionUtils.getData(query);
			//result = HttpConectionUtils.getData(s);
			
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
		    
		    
			CountDownLatch countDownLatch=new CountDownLatch(companyList.size());
			
			ExecutorService executorService=Executors.newFixedThreadPool(CONCURRENT_THREADS);
			
			for (Company company : companyList) {
				executorService.submit(new Processor(countDownLatch,company,resultSet));
			}
			
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			executorService.shutdown();
			
			
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultSet;
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
