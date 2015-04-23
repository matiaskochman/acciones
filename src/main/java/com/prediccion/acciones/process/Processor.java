package com.prediccion.acciones.process;

import java.net.URLEncoder;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.prediccion.acciones.domain.Company;
import com.prediccion.acciones.utils.HttpConectionUtils;

public class Processor implements Runnable{
	
	Integer CONCURRENT_THREADS = 200;
	
	CountDownLatch countDownLatch;
	Company company;
	String data;
	Pattern financial_times_pattern = Pattern.compile("-*\\d+(.)\\d*\\s*%");
	TreeSet<Company> treeSet;

	public Processor(CountDownLatch countDownLatch,Company company,TreeSet<Company> set) {
		super();
		this.company=company;
		this.countDownLatch = countDownLatch;
		this.treeSet = set;
	}

	private void extractFromYqlResult() throws Exception {
		Matcher m = financial_times_pattern.matcher(data);
		 int start1 = 0;
		 int start2 = 0;
		 int start3 = 0;
		 int end1 = 0;
		 int end2 = 0;
		 int end3 = 0;
		 
		 if(m.find()){
			 
			 String string = data;
			 
			 start1 = m.start();
			 end1 = m.end();
			 
			 m.find(end1);
			 
			 start2 = m.start();
			 end2 = m.end();
			 
			 m.find(end2);
			
			 start3 = m.start();
			 end3 = m.end();

			 String val3 = data.substring(start3, end3);
			 String val2 = data.substring(start2, end2);
			 String val1 = data.substring(start1, end1);
			 
			 if(val1.substring(val1.length()-2, val1.length()-1).equals(" ")){
				 val1 = data.substring(start1, end1-2);
			 }else{
				 val1 = data.substring(start1, end1-1);
			 }
			 
			 if(val2.substring(val2.length()-2, val2.length()-1).equals(" ")){
				 val2 = data.substring(start2, end2-2);
			 }else{
				 val2 = data.substring(start2, end2-1);
			 }
			 
			 if(val3.substring(val3.length()-2, val3.length()-1).equals(" ")){
				 val3 = data.substring(start3, end3-2);
			 }else{
				 val3 = data.substring(start3, end3-1);
			 }
			 
			 if(val1 == null || val2 ==null || val3 == null){
				 System.out.println("mal parseado: "+data);
			 }
			 
			 this.company.setMaxForecastValue(new Double(val1)) ;
			 this.company.setMedForecastValue(new Double(val2));
			 this.company.setMinForecastValue(new Double(val3));
			 
			 if(company.getMaxForecastValue()!=null||company.getMinForecastValue()!=null||company.getMedForecastValue()!=null){
				 if(company.getMaxForecastValue().equals(company.getMinForecastValue())&&company.getMaxForecastValue().equals(company.getMedForecastValue())){
					throw new Exception("3 values are equal"); 
				 }
				 
				 treeSet.add(company);
			 }
			 
			 
		 }
	}
	
	@Override
	public void run() {
		try {
			
			
			String newString = "select * from html where url='http://markets.ft.com/research/Markets/Tearsheets/Forecasts?s=YPF:NYQ' and xpath='//table[@class=\"fright\"]/tbody/tr/td[2]/span|//div[@class=\"contains wsodModuleContent\"]/table/tbody/tr/td[1]/span'";
					
			String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
			
			final String financialTimes = "http://markets.ft.com/research/Markets/Tearsheets/Forecasts?s="+this.company.getTicker()+":"+this.company.getMarket();
			final String yql ="select * from html where url='"+financialTimes+"' "+"and xpath='//table[@class=\"fright\"]/tbody/tr/td[2]/span'"; 
			
			final String precioAccion = "//div[@class=\"contains wsodModuleContent\"]/table/tbody/tr/td[1]/span'"; 
			final String porcentajeForecast = "//table[@class=\"fright\"]/tbody/tr/td[2]/span";
			final String valoresForecast = "//table[@class=\"fright\"]/tbody/tr/td[3]";
			
			final String fullUrlStr = baseUrl + URLEncoder.encode(yql, "UTF-8") + "&format=json";
			
			System.out.println(fullUrlStr);
			
			data = HttpConectionUtils.getData(fullUrlStr);
			 extractFromYqlResult();
			System.out.println("equity symbol: "+company.getTicker()+":"+company.getMarket()+"   "+data);
			System.out.println(countDownLatch.getCount());
			
		}catch (NumberFormatException e) {
			
			System.out.println("NumberFormatException en "+company.toString());
			e.printStackTrace();
		}catch (NullPointerException e) {
			
			System.out.println("error en "+company.toString());
			e.printStackTrace();
		}catch (IllegalStateException e) {
			
			System.out.println("error en "+data);
			e.printStackTrace();
		}  catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		countDownLatch.countDown();
		
		
		
		System.out.println("Finished");
	}	
}
