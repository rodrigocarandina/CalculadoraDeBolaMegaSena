package com.tarje.calculadoradebolao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tarje.calculadoradebolao.model.Lottery;
import com.tarje.calculadoradebolao.model.LotteryBet;
import com.tarje.calculadoradebolao.model.LotteryCard;

public class DisplayMessageActivity extends Activity {

	private Lottery lottery;
	private Map<Integer, Integer> bets = new TreeMap<Integer, Integer>();
	private StringBuffer sb;
	private String emailAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Resources res = this.getResources();
		XmlResourceParser xpp = res.getXml(R.xml.bets);
		
		lottery = Lottery.getInstance("Mega-Sena", xpp);
		
		Intent intent = getIntent();
		setContentView(R.layout.activity_display_message);
		
		String message = intent.getStringExtra(MainActivity.AMOUNT_ENTERED_MESSAGE);
		Double amountRaised = Double.valueOf(message);
		LotteryBet[] lotteryBets = lottery.getLotteryBets();
		
		for(int i = lotteryBets.length - 1; i >= 0  && amountRaised > 0;){
			LotteryBet bet = lotteryBets[i];
			Double amount = bet.getValue();
			Integer key = bet.getQuantityOfNumbers();
			
			if(amount <= amountRaised){
				Integer count = Double.valueOf(amountRaised/amount).intValue();
//				Integer count = 1;
				if(bets.containsKey(key)){
					count = bets.remove(key) + count;
				}
				amountRaised = amountRaised - (count * amount);
				bets.put(key, count);
				continue;
			}

			i--;
		
		}
		
		formatBetsString(bets);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}
*/
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	


	
	private void formatBetsString(Map<Integer, Integer> bets){
		TextView textView = (TextView) findViewById(R.id.list_bets);
		StringBuffer sb = new StringBuffer();		
		String betsMessage = getResources().getString(R.string.bets_message);

		Set<Entry<Integer, Integer>> set = bets.entrySet();
		Iterator<Entry<Integer, Integer>> it = set.iterator();
		
		while(it.hasNext()){
			Map.Entry<Integer, Integer> me = (Map.Entry<Integer, Integer>)it.next();
			sb.append(String.format(betsMessage, me.getValue(), me.getKey()));
		}

		textView.setText(sb);
	}

	public void generateNumbers(View view) {
		TextView textView = (TextView) findViewById(R.id.list_selectedNumbers);
		Iterator<Map.Entry<Integer, Integer>> it = bets.entrySet().iterator();
		List<LotteryCard> listOfCards = new ArrayList<LotteryCard>();
		sb = new StringBuffer();
		
		while(it.hasNext()){
			Map.Entry<Integer, Integer> bet = it.next();
			for(int i = 0; i< bet.getValue(); i++){
				LotteryCard card = new LotteryCard(lottery);
				
				card.generateRandomTicket(bet.getKey());
				sb.append(card).append("\n");
				listOfCards.add(card);
			}
		}
		
		textView.setText(sb);		
		textView.setMovementMethod(new ScrollingMovementMethod());

		showEmailViews();
	}

	private void showEmailViews() {
		Button bt = (Button) findViewById(R.id.button_email);
		bt.setVisibility(Button.VISIBLE);
		EditText text = (EditText) findViewById(R.id.email_address);
		text.setVisibility(EditText.VISIBLE);
	}
	
	public void sendEmail(View view){
		EditText editText = (EditText) findViewById(R.id.email_address);
		emailAddress = editText.getText().toString();
		
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
		intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
		intent.setData(Uri.parse("mailto:"+ emailAddress));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
