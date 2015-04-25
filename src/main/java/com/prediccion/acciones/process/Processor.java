package com.prediccion.acciones.process;

import java.net.URLEncoder;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.prediccion.acciones.domain.Company;
import com.prediccion.acciones.utils.HttpConectionUtils;

public class Processor implements Runnable{
	
	CountDownLatch countDownLatch;
	Company company;
	String data;
	Pattern forecast_porcentaje_pattern = Pattern.compile("-*\\d+(.)\\d*\\s*%");
	Pattern forecast_valores_pattern = Pattern.compile("\\\"td\\\":\\s*\\[\\s*\\\"-*\\d+(.)\\d\\d\\\"\\,\\s*\\\"-*\\d+(.)\\d\\d\\\",\\s*\\\"-*\\d+(.)\\d\\d\\\"\\s*\\]");
	Pattern precio_accion_pattern = Pattern.compile("results\\\":\\{\\\"span\\\":\\[\\\"\\d+(.)\\d+");
	
	Pattern volumen_negociado = Pattern.compile("volume_magnitude\",\\s*\\\"content\\\":\\s*\\\"\\d+(.)\\d\\d[mkb]\\\"\\s*}");
	
	
	final String common = "\\\"}..\\\"class\\\":\\\"value\\\",((\\\"style\\\":\\\"color:.........\\\",\\\"content\\\":\\\"\\d+)|(\\\"content\\\":\\\"\\d+))";
	
	Pattern recomendacion_buy = Pattern.compile("Buy"+common);
	Pattern recomendacion_outperform = Pattern.compile("Outperform"+common);
	Pattern recomendacion_hold = Pattern.compile("Hold"+common);
	Pattern recomendacion_underperform = Pattern.compile("Underperform"+common);
	Pattern recomendacion_sell = Pattern.compile("Underperform"+common);
	Pattern recomendacion_no_opinion = Pattern.compile("No opinion"+common);
			
	TreeSet<Company> treeSet;

	public Processor(CountDownLatch countDownLatch,Company company,TreeSet<Company> set) {
		super();
		this.company=company;
		this.countDownLatch = countDownLatch;
		this.treeSet = set;
	}

	private Integer extract_recomendacion(Pattern p){
		Matcher m = p.matcher(data);
		if(m.find()){
			int start=m.start();
			int end=m.end();
			String precio = data.substring(start, end);
			Pattern valor = Pattern.compile("\\d");
			Matcher get_value_pattern = valor.matcher(precio);
			if(get_value_pattern.find()){
				String valor1 = precio.substring(get_value_pattern.start(), get_value_pattern.end());
				 //this.company.setRecomendacionOutPerform(new Integer(valor1)) ;
				return new Integer(valor1);
			}else{
				System.out.println("encontro el match de la recomendacion pero no el valor");
			}
		}else{
			System.out.println("no encontro valor de la recomendacion ");
		}
		return 0;
	}
	
	
	
	private Double extract_precio_accion(Pattern p){
		Matcher m = p.matcher(data);
		
		if(m.find()){
			int start=m.start();
			int end=m.end();
			String precio = data.substring(start, end);
			Pattern valor = Pattern.compile("-*\\d+\\.*\\d*");
			Matcher m_precio_accion = valor.matcher(precio);
			if(m_precio_accion.find()){
				String valor_precio_accion = precio.substring(m_precio_accion.start(), m_precio_accion.end());
				 //this.company.setStockValue(new Double(valor_precio_accion)) ;
				 return new Double(valor_precio_accion);
			}
		}else {
			System.out.println("yql extract -- la accion no tiene precio");
		}
		
		return -1D;
	}
	
	private void extract_forecast_porcentaje() throws Exception {
		Matcher m = forecast_porcentaje_pattern.matcher(data);
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
			 
			 this.company.setMaxForecastPercentageValue(new Double(val1)) ;
			 this.company.setMedForecastPercentageValue(new Double(val2));
			 this.company.setMinForecastPercentageValue(new Double(val3));
			 
			 if(company.getMaxForecastPercentageValue()!=null||company.getMinForecastPercentageValue()!=null||company.getMedForecastPercentageValue()!=null){
				 if(company.getMaxForecastPercentageValue().equals(company.getMinForecastPercentageValue())&&company.getMaxForecastPercentageValue().equals(company.getMedForecastPercentageValue())){
					throw new Exception("3 values are equal"); 
				 }
				 treeSet.add(company);
			 }
		 }
	}
	
	@Override
	public void run() {
		try {
			
			final String precioAccion = "//div[@class=\"contains wsodModuleContent\"]/table/tbody/tr/td[1]/span"; 
			final String porcentajeForecast = "//table[@class=\"fright\"]/tbody/tr/td[2]/span";
			final String valoresForecast = "//table[@class=\"fright\"]/tbody/tr/td[3]";
			
			final String latestRecomendations = "//div[@class=\"wsodRecommendationRating wsodModuleLastInGridColumn\"]/table";
			String newString = "select * from html where url='http://markets.ft.com/research/Markets/Tearsheets/Forecasts?s=YPF:NYQ' and xpath='//table[@class=\"fright\"]/tbody/tr/td[2]/span|//div[@class=\"contains wsodModuleContent\"]/table/tbody/tr/td[1]/span'";
					
			String baseUrl = "http://query.yahooapis.com/v1/public/yql?q=";
			
			final String financialTimes = "http://markets.ft.com/research/Markets/Tearsheets/Forecasts?s="+this.company.getTicker()+":"+this.company.getMarket();
			final String yql ="select * from html where url='"+financialTimes+"' "+"and xpath='"+porcentajeForecast+"|"+latestRecomendations+"|"+precioAccion+"'"; 
			
			
			final String fullUrlStr = baseUrl + URLEncoder.encode(yql, "UTF-8") + "&format=json";
			
			System.out.println(fullUrlStr);
			
			data = HttpConectionUtils.getData(fullUrlStr);
			
			extract_forecast_porcentaje();
			company.setStockValue(extract_precio_accion(precio_accion_pattern));
			
			company.setRecomendacionBuy(extract_recomendacion(recomendacion_buy));
			company.setRecomendacionOutPerform(extract_recomendacion(recomendacion_outperform));
			company.setRecomendacionHold(extract_recomendacion(recomendacion_hold));
			company.setRecomendacionUnderPerform(extract_recomendacion(recomendacion_underperform));
			company.setRecomendacionSell(extract_recomendacion(recomendacion_sell));
			company.setRecomendacionNoOpinion(extract_recomendacion(recomendacion_no_opinion));
			
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
