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
    	Set<Company> list = parsingService.getSocksFromGoogleFinance();
    	
    	for (Company company : list) {
			System.out.println(company);
		}
    	
    }
    
}
