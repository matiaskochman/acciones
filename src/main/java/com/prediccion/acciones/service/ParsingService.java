package com.prediccion.acciones.service;

import java.util.List;

import com.prediccion.acciones.domain.Company;



//@Service
public interface ParsingService {

	public List<Company> getSocksFromGoogleFinance();
}
