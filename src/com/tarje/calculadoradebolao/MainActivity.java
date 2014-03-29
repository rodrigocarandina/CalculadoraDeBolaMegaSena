package com.tarje.calculadoradebolao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends Activity {

	private static final int MINIMUM_BET = 2;
	private static final int MAX_AMOUNT_OF_BET = 1000000;
	public final static String AMOUNT_ENTERED_MESSAGE = "com.tarje.calculadoradebolao.AMOUNT_ENTERED_MESSAGE";
	private AdView adView;
	private static final String AD_UNIT_ID = "ca-app-pub-6793881900708870/2342031947";
	
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


/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
	/** Called when the user clicks the Send button */
	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.amount_raised_message);
		String betAmount = editText.getText().toString();
		if(!"".equals(betAmount) && null != betAmount && 
				Double.valueOf(betAmount) >= MINIMUM_BET && Double.valueOf(betAmount) <= MAX_AMOUNT_OF_BET){
			intent.putExtra(AMOUNT_ENTERED_MESSAGE, betAmount);
			startActivity(intent);
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
	

}
