package com.otitan.entity;

public class WeatherEntity
{
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public String getTmp()
	{
		return tmp;
	}
	public void setTmp(String tmp)
	{
		this.tmp = tmp;
	}
	public String getHumidity()
	{
		return humidity;
	}
	public void setHumidity(String humidity)
	{
		this.humidity = humidity;
	}
	public String getDirection()
	{
		return direction;
	}
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	public String getMaxtmp()
	{
		return maxtmp;
	}
	public void setMaxtmp(String maxtmp)
	{
		this.maxtmp = maxtmp;
	}
	public String getMintmp()
	{
		return mintmp;
	}
	public void setMintmp(String mintmp)
	{
		this.mintmp = mintmp;
	}
	String time;
	String tmp;
	String humidity;//湿度
	String direction;//风向
	String maxtmp;//最高温度
	String mintmp;//最低温度
	String speed;
	String description;
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getSpeed()
	{
		return speed;
	}
	public void setSpeed(String speed)
	{
		this.speed = speed;
	}//风速
}
