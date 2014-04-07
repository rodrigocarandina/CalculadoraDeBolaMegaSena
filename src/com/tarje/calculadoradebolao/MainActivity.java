package com.tarje.calculadoradebolao;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tarje.calculadoradebolao.model.Lottery;
import com.tarje.calculadoradebolao.model.LotteryBet;

public class MainActivity extends Activity {

	public static final String CALCULATED_BETS = "Bets";

	private static final int MINIMUM_BET = 2;
	private static final int MAX_AMOUNT_OF_BET = 1000000;
	private AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-6793881900708870/2342031947";
	private TreeMap<Integer, Integer> bets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		addAdvertisement();
	}


	private void addAdvertisement() {
		if(ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()))
		{
			adView = new AdView(this);

			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(AD_UNIT_ID);

			RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, 
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

			layout.addView(adView, params);

			// Create an ad request. Check logcat output for the hashed device ID to
			// get test ads on a physical device.
			AdRequest adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
			.build();

			// Start loading the ad in the background.
			adView.loadAd(adRequest);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Resources res = this.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.bets);

		Lottery lottery = Lottery.getInstance("Mega-Sena", xpp);
		bets = new TreeMap<Integer, Integer>();
		
		EditText editText = (EditText) findViewById(R.id.amount_raised_message);
		String amountInput = editText.getText().toString();
		if("".equals(amountInput) || null == amountInput)
		
		{
			((TextView) findViewById(R.id.list_bets)).setText(null);
			setGenerateButtonVisibility(Button.INVISIBLE);
		}
		
		try{
			Double amountRaised = Double.valueOf(amountInput);
			if(amountRaised > MAX_AMOUNT_OF_BET || amountRaised < MINIMUM_BET){
				((TextView) findViewById(R.id.list_bets)).setText(null);
				setGenerateButtonVisibility(Button.INVISIBLE);
				return false;
			}
			LotteryBet[] lotteryBets = lottery.getLotteryBets();

			for(int i = lotteryBets.length - 1; i >= 0  && amountRaised > 0;){
				LotteryBet bet = lotteryBets[i];
				Double amount = bet.getValue();
				Integer key = bet.getQuantityOfNumbers();

				if(amount <= amountRaised){
					Integer count = Double.valueOf(amountRaised/amount).intValue();

					if(bets.containsKey(key)){
						count = bets.remove(key) + count;
					}
					amountRaised = amountRaised - (count * amount);
					bets.put(key, count);
					continue;
				}

				i--;

			}
			formatBetsString(bets, amountRaised);
			setGenerateButtonVisibility(Button.VISIBLE);
		}catch(NumberFormatException nfe){

		}

		return true;
	}


	private void setGenerateButtonVisibility(Integer visibility) {
		Button bt = (Button) findViewById(R.id.button_generate);
		bt.setVisibility(visibility);
	}

	private void formatBetsString(Map<Integer, Integer> bets, Double amountRaised){
		TextView textView = (TextView) findViewById(R.id.list_bets);
		StringBuffer sb = new StringBuffer();		
		String betsMessage = getResources().getString(R.string.bets_message);

		Set<Entry<Integer, Integer>> set = bets.entrySet();
		Iterator<Entry<Integer, Integer>> it = set.iterator();

		while(it.hasNext()){
			Map.Entry<Integer, Integer> me = (Map.Entry<Integer, Integer>)it.next();
			sb.append(String.format(betsMessage, me.getValue(), me.getKey()));
		}
		if(amountRaised > 0){
			sb.append(String.format(getResources().getString(R.string.bet_change), amountRaised));
		}

		textView.setText(sb);
		textView.setTypeface(null, Typeface.BOLD_ITALIC);
	}

	public void generateNumbers(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(CALCULATED_BETS, bets);
		startActivity(intent);
	}

}
