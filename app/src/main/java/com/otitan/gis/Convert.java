package com.otitan.gis;

public class Convert
{
	/** 
	  * 将角度转换为度分秒得字符串表示形式 
	  * @param angle 
	  * @return 
	  */ 
	 public static String convert(double angle) 
	 { 
	  StringBuffer transAngle = new StringBuffer(); 
	  int deg = (int) angle; 
	  if (deg > 0) 
	  { 
	   transAngle.append(deg + "°"); 
	  }  
	  double mi = angle - deg; 
	  //得到分 
	  int minute = (int)(mi*60); 
	  transAngle.append(minute + "′"); 
	  double se = mi*60 - minute; 
	  transAngle.append(se * 60 + "″"); 
	  return transAngle.toString(); 
	 } 
	  
	 /** 
	  * 度分秒的字符串表示形式转换为数值格式 
	  * @param angle 
	  * @return 
	  */ 
	 public static double convertToAngle(String angle) 
	 { 
	  StringBuffer transAngle = new StringBuffer(angle); 
	  //获得度分秒的字符串 
	  String degreeString = transAngle.substring(0, transAngle.indexOf("°")); 
	  String minuteString = transAngle.substring(transAngle.indexOf("°")+1, 
	    transAngle.indexOf("′")); 
	  String secondString = transAngle.substring(transAngle.indexOf("′")+1, 
	    transAngle.indexOf("″")); 
	  //判断是否符合数值格式 
	  double degree = 0; 
	  double minute = 0; 
	  double second = 0; 
	/*  if (checkNum(degreeString)&&checkNum(minuteString)&&checkNum(secondString)) 
	  { 
	   degree = Double.parseDouble(degreeString); 
	   minute = Double.parseDouble(minuteString); 
	   second = Double.parseDouble(secondString); 
	  } */
	
	   degree = Double.parseDouble(degreeString); 
	   minute = Double.parseDouble(minuteString); 
	   second = Double.parseDouble(secondString); 
	  
	  return degree + (minute*60+second)/3600.0; 
	 } 
}
