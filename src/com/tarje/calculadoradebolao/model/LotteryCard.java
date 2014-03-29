package com.tarje.calculadoradebolao.model;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class LotteryCard {

	private Set<Integer> selectedNumbers = new TreeSet<Integer>();
	private Lottery lottery;
	
	public LotteryCard(Lottery lottery){
		this.lottery = lottery;
	}
	
	public boolean selectNumber(Integer drawNumber){
		return selectedNumbers.add(drawNumber);
	}
	
	public Integer[] generateRandomTicket(Integer quantityOfSelectedNumbers){
		for(int i = 0; i < quantityOfSelectedNumbers; i++){
			while(!selectNumber(new Random().nextInt(lottery.getHighestNumberToBeDraw() - lottery.getLowestNumberToBeDraw() + 1)+1));
		}
		return selectedNumbers.toArray(new Integer[selectedNumbers.size()]);
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		for(Integer i : selectedNumbers){
			sb.append(i + " " + " - " + " ");
		}
		sb.delete(sb.length() -3, sb.length());
		return sb.toString();
	}
	
}
