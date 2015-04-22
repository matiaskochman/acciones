package com.prediccion.acciones.domain;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

import org.springframework.roo.addon.json.RooJson;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class Company {

	

	public Company(String title, String ticker, String exchange,
			String companyId, String localCurrencySymbol,
			List<CompanyProperty> properties) {
		super();
		this.title = title;
		this.ticker = ticker;
		this.exchange = exchange;
		this.companyId = companyId;
		this.localCurrencySymbol = localCurrencySymbol;
		this.properties = properties;
		setMarket(exchange);
	}

	/**
     */
    @NotNull
    @Column(unique = true)
    private String title;

    /**
     */
    @NotNull
    @Column(unique = true)
    private String ticker;

    @Column
    private Double maxForecastValue;
    @Column
    private Double medForecastValue;
    @Column
    private Double minForecastValue;
    
    /**
     */
    @NotNull
    private String exchange;

    @NotNull
    private String market;
    
    /**
     */
    @Column(unique = true)
    private String companyId;

    /**
     */
    private String localCurrencySymbol;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<CompanyProperty> properties = new ArrayList<CompanyProperty>();
    
    public void setMarket(String exchange){
    	if(exchange.equalsIgnoreCase("NYSE")){
    		this.market = "NYQ";
    	}else if(exchange.equalsIgnoreCase("NASDAQ")){
    		this.market = "NSQ";
    	}else if(exchange.equalsIgnoreCase("LON")){
    		this.market = "LSE";
    	}else if(exchange.equalsIgnoreCase("EPA")){
    		this.market = "PAR";
    	}
    }


	@Override
	public String toString() {
		return "Company [ticker=" + ticker + ", market="
				+ market + "]"+ ", maxForecastValue="
				+ maxForecastValue + ", medForecastValue=" + medForecastValue
				+ ", minForecastValue=" + minForecastValue +", title=    "+title;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((exchange == null) ? 0 : exchange.hashCode());
		result = prime * result + ((ticker == null) ? 0 : ticker.hashCode());
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
		Company other = (Company) obj;
		if (exchange == null) {
			if (other.exchange != null)
				return false;
		} else if (!exchange.equals(other.exchange))
			return false;
		if (ticker == null) {
			if (other.ticker != null)
				return false;
		} else if (!ticker.equals(other.ticker))
			return false;
		return true;
	}
    
    
}
