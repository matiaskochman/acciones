// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.prediccion.acciones.controller;

import com.prediccion.acciones.controller.ApplicationConversionServiceFactoryBean;
import com.prediccion.acciones.domain.Company;
import com.prediccion.acciones.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    @Autowired
    CompanyService ApplicationConversionServiceFactoryBean.companyService;
    
    public Converter<Company, String> ApplicationConversionServiceFactoryBean.getCompanyToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.prediccion.acciones.domain.Company, java.lang.String>() {
            public String convert(Company company) {
                return new StringBuilder().append(company.getTitle()).append(' ').append(company.getTicker()).append(' ').append(company.getMarket()).append(' ').append(company.getStockValue()).toString();
            }
        };
    }
    
    public Converter<Long, Company> ApplicationConversionServiceFactoryBean.getIdToCompanyConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.prediccion.acciones.domain.Company>() {
            public com.prediccion.acciones.domain.Company convert(java.lang.Long id) {
                return companyService.findCompany(id);
            }
        };
    }
    
    public Converter<String, Company> ApplicationConversionServiceFactoryBean.getStringToCompanyConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.prediccion.acciones.domain.Company>() {
            public com.prediccion.acciones.domain.Company convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Company.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getCompanyToStringConverter());
        registry.addConverter(getIdToCompanyConverter());
        registry.addConverter(getStringToCompanyConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
