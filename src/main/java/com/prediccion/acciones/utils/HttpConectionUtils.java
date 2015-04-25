package com.prediccion.acciones.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class HttpConectionUtils {

		
	
	public static String getData(String address) throws Exception {
		
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("172.18.3.100", 8080));
		//HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection(proxy);
		HttpURLConnection conn = (HttpURLConnection) new URL(address).openConnection();
		conn.setConnectTimeout(10000);
		  
	    URL url = new URL(address);
	    StringBuffer text = new StringBuffer();
	    StringBuilder sb = new StringBuilder();
	    
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		
	    //conn.connect();
	    InputStreamReader in = new InputStreamReader((InputStream) conn.getContent());
	    BufferedReader buff = new BufferedReader(in);
	    String line;
	    do {
	      line = buff.readLine();
	      sb.append(line);
	    } while (line != null);
	    
	    in.close();
	    
	    return sb.toString();
	}	

}
