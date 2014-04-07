package com.tarje.calculadoradebolao.model;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class CustomizedList<T> extends ArrayList<T>{

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		for(T object : this){
			if(object instanceof LotteryCard){
				buffer.append(object.toString()).append("\n");
			}
				
		}
		
		return buffer.toString();
	}
	
}
