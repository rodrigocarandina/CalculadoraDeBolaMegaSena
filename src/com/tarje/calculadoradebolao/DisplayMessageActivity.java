package com.tarje.calculadoradebolao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tarje.calculadoradebolao.model.CustomizedList;
import com.tarje.calculadoradebolao.model.Lottery;
import com.tarje.calculadoradebolao.model.LotteryCard;

public class DisplayMessageActivity extends Activity {

	private Lottery lottery;
	private TreeMap<Integer, Integer> bets;
	private StringBuffer sb;
	private String emailAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		setContentView(R.layout.activity_display_message);

		@SuppressWarnings("unchecked")
		HashMap<Integer, Integer> hash = (HashMap<Integer, Integer>) intent.getSerializableExtra(MainActivity.CALCULATED_BETS);
		bets = new TreeMap<Integer, Integer>(hash);

		XmlResourceParser xpp = this.getResources().getXml(R.xml.bets);

		lottery = Lottery.getInstance("Mega-Sena", xpp);
		displayGeneratedNumbers();
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

	public void regenerateNumbers(View view){
			displayGeneratedNumbers();
	}

	private void displayGeneratedNumbers(){
		TextView textView = (TextView) findViewById(R.id.list_selectedNumbers);
		Iterator<Map.Entry<Integer, Integer>> it = bets.entrySet().iterator();
		final List<LotteryCard> listOfCards = new CustomizedList<LotteryCard>();
		
		sb = new StringBuffer();
		List<Thread> threadList = new ArrayList<Thread>();
		
		while(it.hasNext()){
			final Map.Entry<Integer, Integer> bet = it.next();
			for(int i = 0; i< bet.getValue(); i++){
				Thread randomTicketGeneratorThread = new Thread(){			
					public void run(){
						LotteryCard card = new LotteryCard(lottery);
				
						card.generateRandomTicket(bet.getKey());
						listOfCards.add(card);

					}};
					randomTicketGeneratorThread.start();
					threadList.add(randomTicketGeneratorThread);
			}
		}
		for (Thread thread : threadList) {
			try{
				thread.join();
			}catch(InterruptedException ie){}			  
		}
		
		Collections.sort(listOfCards);
		
		textView.setText(listOfCards.toString());
		textView.setMovementMethod(new ScrollingMovementMethod());
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