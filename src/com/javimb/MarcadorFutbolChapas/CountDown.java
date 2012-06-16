package com.javimb.MarcadorFutbolChapas;

import android.os.CountDownTimer;
import android.widget.TextView;

public class CountDown {
	private TextView text;
	private int min, sec, startMin, startSec;
	private boolean running;
	private CountDownTimer timer;
	
	public CountDown(TextView text, int startMin, int startSec) {
		this.text = text;
		this.startMin = startMin;
		this.startSec = startSec;		
		this.min = startMin;
		this.sec = startSec;		
		this.running = false;
		initText();
	}
	
	private void createTimer(int minutes, int seconds){
		timer = new CountDownTimer(minSecToMillis(minutes, seconds), 1000) {

		     public void onTick(long millisUntilFinished) {
		    	 text.setText(millisTominSec(millisUntilFinished));
		     }

		     public void onFinish() {
		    	 running = false;
		    	 reset();
		     }
		  };
	}
	
	public boolean start(){
		if(!isRunning()){
			createTimer(min, sec);
			timer.start();
			running = true;
			return true;
		}
		return false;
	}
	
	public boolean pause(){
		if(isRunning()){
			timer.cancel();
			running = false;
			return true;
		}
		return false;
	}
	
	public boolean reset(){
		if(!isRunning()){
			min = startMin;
			sec = startSec;
			initText();
			createTimer(min, sec);
			return true;
		}
		return false;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	private void initText(){
		String minSec = "";
		if(min < 10){
			minSec += "0";
		}
		minSec += min + ":";
		if(sec < 10){
			minSec += "0";
		}
		minSec += sec;
		text.setText(minSec);
	}
	
	private int minSecToMillis(int min, int sec){
		int millis = min * 60 * 1000;
		millis += sec * 1000;
		return millis;
	}
	
	private String millisTominSec(long millis){
		String minSec = "";
		long min = millis / 60000;
		this.min = (int) min;
		long sec = millis % 60000 / 1000;
		this.sec = (int) sec;
		
		if(min < 10){
			minSec += "0";
		}
		minSec += min + ":";
		if(sec < 10){
			minSec += "0";
		}
		minSec += sec;
		
		return minSec;
	}
}
