package com.tarje.calculadoradebolao.model;

public class LotteryBet {
	
	private Double value;
	private Integer quantityOfNumbers;
	
	public LotteryBet(Integer quantityOfNumber, Double value){
		this.quantityOfNumbers = quantityOfNumber;
		this.value = value;
	}
		
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Integer getQuantityOfNumbers() {
		return quantityOfNumbers;
	}
	public void setQuantityOfNumbers(Integer quantityOfNumbers) {
		this.quantityOfNumbers = quantityOfNumbers;
	}

}
