package com.javimb.MarcadorFutbolChapas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MarcadorFutbolChapasActivity extends Activity implements OnClickListener, OnLongClickListener, OnSharedPreferenceChangeListener {
	private SharedPreferences prefs;
	private TextView leftScoreboard, rightScoreboard, separator, leftName, rightName, chrono;
	private int rigthValue, leftValue;
	private Button startChrono, pauseChrono, resetChrono;
	private CountDown countDown;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
        
        leftScoreboard = (TextView) findViewById(R.scoreboard.left);
        rightScoreboard = (TextView) findViewById(R.scoreboard.rigth);
        separator = (TextView) findViewById(R.scoreboard.separator);
        leftName = (TextView) findViewById(R.names.left);
        rightName = (TextView) findViewById(R.names.right);
        startChrono = (Button) findViewById(R.buttons.startChrono);
        resetChrono = (Button) findViewById(R.buttons.resetChrono);
        pauseChrono = (Button) findViewById(R.buttons.pauseChrono);
        chrono = (TextView) findViewById(R.id.chrono);
        
        rightScoreboard.setText(Integer.toString(rigthValue));
        leftScoreboard.setText(Integer.toString(leftValue));
        leftName.setText(prefs.getString("home", getString(R.string.homeDefaultName)));
        rightName.setText(prefs.getString("away", getString(R.string.awayDefaultName)));
        
        leftScoreboard.setOnLongClickListener(this);
        rightScoreboard.setOnLongClickListener(this);
        separator.setOnLongClickListener(this);
        
        startChrono.setOnClickListener(this);
        pauseChrono.setOnClickListener(this);
        resetChrono.setOnClickListener(this);
        
        try {
			countDown = new CountDown(chrono, Integer.parseInt(prefs.getString("min", "15")),
					Integer.parseInt(prefs.getString("sec", "0")));
		} catch (NumberFormatException e) {
			countDown = new CountDown(chrono, 15, 0);
		}
    }
    
    public void increase(View v){
    	switch(v.getId()){
			case R.buttons.increaseRight:
				rigthValue++;
				rightScoreboard.setText(Integer.toString(rigthValue));
				break;

			case R.buttons.increaseLeft:
				leftValue++;
				leftScoreboard.setText(Integer.toString(leftValue));
				break;
		}
    }
    
    public void decrease(View v){
    	switch(v.getId()){
			case R.buttons.decreaseRight:
				if(rigthValue > 0){
					rigthValue--;
					rightScoreboard.setText(Integer.toString(rigthValue));
				}
				break;

			case R.buttons.decreaseLeft:
				if(leftValue > 0){
					leftValue--;
					leftScoreboard.setText(Integer.toString(leftValue));
				}
				break;
		}
    }
    
    public void reverse(View v){
    	int rightValueAux = rigthValue;
    	rigthValue = leftValue;
    	leftValue = rightValueAux;
    	rightScoreboard.setText(Integer.toString(rigthValue));
    	leftScoreboard.setText(Integer.toString(leftValue));
    	
    	String rightNameAux = (String) rightName.getText();
    	rightName.setText(leftName.getText());
    	leftName.setText(rightNameAux);
    }
    
    public void reset(View v){
    	switch(v.getId()){
			case R.scoreboard.rigth:
				rigthValue = 0;
				rightScoreboard.setText(Integer.toString(rigthValue));
				break;

			case R.scoreboard.left:
				leftValue = 0;
				leftScoreboard.setText(Integer.toString(leftValue));
				break;
			
			case R.scoreboard.separator:
				leftValue = 0;
				leftScoreboard.setText(Integer.toString(leftValue));
				rigthValue = 0;
				rightScoreboard.setText(Integer.toString(rigthValue));
				break;	
		}
    }
    
    public void onClick(View v) {
		switch (v.getId()) {
		case R.buttons.startChrono:
			countDown.start();
			break;
			
		case R.buttons.pauseChrono:
			countDown.pause();
			break;
			
		case R.buttons.resetChrono:
			countDown.reset();
			break;
		}
	}

	public boolean onLongClick(View v) {
		reset(v);
		return false;
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if(key.equals("home")){
			leftName.setText(prefs.getString("home", getString(R.string.homeDefaultName)));
			return;
		}
		if(key.equals("away")){
			rightName.setText(prefs.getString("away", getString(R.string.awayDefaultName)));
			return;
		}
		if(key.equals("min") || key.equals("sec")){
			try {
				countDown = new CountDown(chrono, Integer.parseInt(prefs.getString("min", "15")),
						Integer.parseInt(prefs.getString("sec", "0")));
			} catch (NumberFormatException e) {
				Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.chronoError), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER,0,0);
				toast.show();
			}
			return;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
			
		case R.id.itemShare:
			String shareText = leftName.getText() + " " + leftValue + " - " + rigthValue + " " + rightName.getText() + ", usando Marcador de FutbolChapas http://bit.ly/marcadorfch";
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
			startActivity(Intent.createChooser(shareIntent, getText(R.string.shareTitle)));
			break;

		case R.id.itemInfo:
			Dialog dialogo = new Dialog(this);
			dialogo.setContentView(R.layout.info);
			dialogo.setTitle(R.string.infoTitle);
			dialogo.show();
			break;
		}
		return true;
	}
}