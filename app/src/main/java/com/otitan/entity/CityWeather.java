package com.otitan.entity;
import java.util.List;
public class CityWeather
{



	   


	   private String city;
	   private String cod;
	   private double message;
	   private int cnt;
	   private List<List> list;


	    public void setCity(String city) {
	        this.city = city;
	    }
	    public String getCity() {
	        return city;
	    }
	    

	    public void setCod(String cod) {
	        this.cod = cod;
	    }
	    public String getCod() {
	        return cod;
	    }
	    

	    public void setMessage(double message) {
	        this.message = message;
	    }
	    public double getMessage() {
	        return message;
	    }
	    

	    public void setCnt(int cnt) {
	        this.cnt = cnt;
	    }
	    public int getCnt() {
	        return cnt;
	    }
	    

	    public void setList(List<List> list) {
	        this.list = list;
	    }
	    public List<List> getList() {
	        return list;
	    }
	    
	}

