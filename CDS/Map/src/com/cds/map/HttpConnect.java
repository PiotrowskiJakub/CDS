/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.map;

import com.google.gson.Gson;
import java.io.IOException;
import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sebastian
 */
public class HttpConnect {
    
    public HttpResponse http(double[][] coordinates) throws JSONException {
    	
    	Stock stock = new Stock(coordinates);
    	
        try{
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("http://exeter.tk/coords2details");
            StringEntity params = new StringEntity(new Gson().toJson(stock));
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            Gson gson = new com.google.gson.Gson();
            System.out.println(json.toString());
            
            //bo json ma sie zaczynac od {, a nie od [ wiec sie pozbywam nawiasow []
            String newJson = json.substring(1);
            newJson = newJson.substring(0,newJson.length()-1);
            System.out.println(newJson.toString());
            
            
            JSONObject obj = new JSONObject(newJson);
            String terrain = obj.getJSONObject("statistics").getString("terrain");
            //units nie trzeba, bo wszedzie sa metry
            String units = obj.getJSONObject("statistics").getJSONObject("elevation").getString("units");
            String value = obj.getJSONObject("statistics").getJSONObject("elevation").getString("value");
            
            System.out.println("terrain: " + terrain);
            System.out.println("units: " + units);
            System.out.println("value: " + value);

        } catch (IOException ex) {
        }
        return null;
    }
    
    public class Stock {
    	double[][] locations;
    	
    	public Stock(double[][] locations){
    		this.locations = locations;
    	}
    	
    	public double[][] getLocation(){
    		return locations;
    	}
    
    
    }
}
