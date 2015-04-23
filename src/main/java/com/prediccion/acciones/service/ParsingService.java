package com.prediccion.acciones.service;

import java.util.Set;

import com.prediccion.acciones.domain.Company;



//@Service
public interface ParsingService {

	public Set<Company> getSocksFromGoogleFinance(String address);
}
