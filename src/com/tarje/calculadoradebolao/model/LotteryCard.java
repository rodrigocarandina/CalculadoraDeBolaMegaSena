package com.tarje.calculadoradebolao.model;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class LotteryCard implements Comparable<LotteryCard>{

	private Set<Integer> selectedNumbers = new TreeSet<Integer>();
	private Lottery lottery;
	
	public LotteryCard(Lottery lottery){
		this.lottery = lottery;
	}
	
	private boolean markNumber(Integer drawNumber){
		return selectedNumbers.add(drawNumber);
	}
	
	public Integer[] generateRandomTicket(Integer quantityOfSelectedNumbers){
		for(int i = 0; i < quantityOfSelectedNumbers; i++){
			while(!markNumber(generateRandomNumber()));
		}
		return selectedNumbers.toArray(new Integer[selectedNumbers.size()]);
	}

	private int generateRandomNumber() {
		return new Random().nextInt(lottery.getHighestNumberToBeDraw() - lottery.getLowestNumberToBeDraw() + 1)+1;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		for(Integer i : selectedNumbers){
			sb.append(i + " " + " - " + " ");
		}
		sb.delete(sb.length() -4, sb.length());
		return sb.toString();
	}

	@Override
	public int compareTo(LotteryCard lotteryCard) {
		return Integer.valueOf(this.selectedNumbers.size()).compareTo(lotteryCard.selectedNumbers.size());
	}
	
}