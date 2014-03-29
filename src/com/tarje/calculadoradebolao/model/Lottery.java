package com.tarje.calculadoradebolao.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;

public class Lottery{

	private static Map<String, Lottery> instanceMap = new HashMap<String, Lottery>();
	
	private List<LotteryBet> bets;
	private String name;
	private Integer lowestNumberToBeDrawNumber;
	private Integer highestNumberToBeDrawNumber;
	
	private static final String XML_HIGHEST_NUMBER_TO_BE_DRAW = "highestNumberToBeDraw";
	private static final String XML_LOWEST_NUMBER_TO_BE_DRAW = "lowestNumberToBeDraw";
	private static final String XML_BET = "bet";
	private static final String XML_BETS = "bets";
	private static final String XML_LOTTERY = "lottery";
	
	private Lottery(){
		
	}
	
	private Lottery(String name, XmlResourceParser xpp){
		this.name=name;
		readLotteryConfigurationFromXML(this, xpp);
	}
	
	public static synchronized Lottery getInstance(String name, XmlResourceParser xpp){
		if(!instanceMap.containsKey(name)){
			instanceMap.put(name, new Lottery(name, xpp));
		}
		return instanceMap.get(name);
	}
	
	
	public void addLotteryBet(LotteryBet lotteryBet){
		if(bets == null){
			bets = new ArrayList<LotteryBet>();
		}
		bets.add(lotteryBet);
	}
	
	public LotteryBet getLotteryBet(int i) {
	    return bets.get(i);
	}
	
	public LotteryBet[] getLotteryBets() {
		LotteryBet[] elements = new LotteryBet[bets.size()];
		bets.toArray(elements);        
	    return elements;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLowestNumberToBeDraw() {
		return lowestNumberToBeDrawNumber;
	}
	public void setLowestNumberToBeDraw(Integer firstDrawNumber) {
		this.lowestNumberToBeDrawNumber = firstDrawNumber;
	}
	public Integer getHighestNumberToBeDraw() {
		return highestNumberToBeDrawNumber;
	}
	public void setHighestNumber(Integer lastDrawNumber) {
		this.highestNumberToBeDrawNumber = lastDrawNumber;
	}
	
	private static Lottery readLotteryConfigurationFromXML(Lottery lottery, XmlResourceParser xpp)
	{
		try{
			xpp.next();
			int eventType = xpp.getEventType();
	
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				if(eventType == XmlPullParser.START_TAG)
				{
					if(xpp.getName().equals(XML_LOTTERY)){
						if(!xpp.getAttributeValue(0).equals(lottery.getName())){
							eventType = xpp.next();
						}
					
						else{
							while(eventType != XmlPullParser.END_DOCUMENT){
								eventType = xpp.next();
								if(XML_BETS.equals(xpp.getName()) && eventType == XmlPullParser.START_TAG){
									eventType = xpp.next();
									while(xpp.getName().equals(XML_BET) && eventType == XmlPullParser.START_TAG)
									{
										LotteryBet bet = new LotteryBet(Integer.valueOf(xpp.getAttributeValue(0)), Double.valueOf(xpp.getAttributeValue(1)));
										lottery.addLotteryBet(bet);
										xpp.next();
										eventType = xpp.next();
										
									}
								}
								else if(XML_LOWEST_NUMBER_TO_BE_DRAW.equals(xpp.getName()) && eventType == XmlPullParser.START_TAG){
									lottery.setLowestNumberToBeDraw(Integer.valueOf(xpp.getAttributeValue(0)));
								}
								else if(XML_HIGHEST_NUMBER_TO_BE_DRAW.equals(xpp.getName()) && eventType == XmlPullParser.START_TAG){
									lottery.setHighestNumber(Integer.valueOf(xpp.getAttributeValue(0)));
								}
								eventType = xpp.next();
							}
						}
					}
				}
				eventType = xpp.next();
			}
			xpp.close();
		}catch(Exception exception){
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(6), Double.valueOf(2)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(7), Double.valueOf(14)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(8), Double.valueOf(56)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(9), Double.valueOf(168)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(10), Double.valueOf(420)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(11), Double.valueOf(924)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(12), Double.valueOf(1848)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(13), Double.valueOf(3432)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(14), Double.valueOf(6006)));
			lottery.addLotteryBet(new LotteryBet(Integer.valueOf(15), Double.valueOf(10010)));
		}
		
		return lottery;
	}

}
