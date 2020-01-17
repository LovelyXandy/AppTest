package com.mycompany.myapp2;

import android.app.*;
import android.widget.*;
import android.view.*;
import android.os.*;
import android.speech.tts.*;
import java.util.Locale;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

public class MainActivity extends Activity 
{
	TextView answerInput;
	TextView question;
	AlertDialog.Builder builder;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	int answer;
	int integer1;
	int integer2;
	int maxSize = 20;
	boolean addition;
	Random generator = new Random();
	int score = 0;
	TextToSpeech talker;
	boolean active;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		builder = new AlertDialog.Builder(this);  
		talker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {public void onInit(int status) {}
		});
		final Button button = findViewById(R.id.button1);
		final Button button2 = findViewById(R.id.button2);
		answerInput = findViewById(R.id.editAnswer);
		question = findViewById(R.id.textView1);
		
		question.setText("Created");
		//talker.speak("Welcome Sam to the maths quiz, press Go for a question", TextToSpeech.QUEUE_ADD, null);
		
		button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					talker.setLanguage(Locale.UK);
					talker.speak("Go", TextToSpeech.QUEUE_ADD, null);
					active = true;
					askQuestion();
				}
			});
			
		button2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					talker.setLanguage(Locale.UK);
					talker.speak("Thanks for playing! Your score was " + score, TextToSpeech.QUEUE_ADD, null);
					active = false;
				}
			});
    }

	@Override
	public void onStateNotSaved()
	{
		// TODO: Implement this method
		super.onStateNotSaved();
	}

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		talker.setLanguage(Locale.UK);
		question.setText("Ready");
		waitABit(50);
		talker.speak("Welcome Sam to the maths quiz, press Go for a question", TextToSpeech.QUEUE_FLUSH, null);
		waitToFinishTalking();
	
	}
	
	private void waitABit(int timeWait){
		
		try{
			Thread.sleep(timeWait);
		}catch(InterruptedException e){}
	}
	
	private void waitToFinishTalking(){
		
		while(talker.isSpeaking()){
			waitABit(100);
		}
	}
	
	private void askQuestion(){
	
		doQuestion();
		waitToFinishTalking();
		askSpeechInput();
		
	}
	
	// Showing google speech input dialog
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-GB");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
						"Hi say something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
					if (resultCode == RESULT_OK && null != data) {

						ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
						answerInput.setText(result.get(0));
						//answerInput.draw();
						waitABit(500);
						checkAnswer();
					}else{
						if(active){
						askSpeechInput();
						}
					}
					break;
				}

        }
		
	}
	
	protected void checkAnswer(){
		try{
		if (answer== Integer.parseInt(answerInput.getText().toString().replace("—","-").replace(" ",""))){
		score++;
		talker.speak("Correct! Your score is " + score, TextToSpeech.QUEUE_ADD, null);
		}else{
			
			talker.speak("Not quite - the correct answer was " + answer, TextToSpeech.QUEUE_ADD, null);
		}
		}catch(NumberFormatException e){
			//Not a number
			talker.speak("Sorry " + answerInput.getText() + " wasn't recognised as a number.",TextToSpeech.QUEUE_ADD, null); 
		}
		waitToFinishTalking();
		if(active){askQuestion();}
	}
	
	protected void doQuestion(){
		
		integer1 = generator.nextInt(maxSize);
		integer2 = generator.nextInt(maxSize);
		addition = generator.nextBoolean();
		
		if(addition) {
			answer = integer1 + integer2;
			talker.speak("What is " + integer1 + " plus " + integer2, TextToSpeech.QUEUE_ADD, null);
			question.setText("What is " + integer1 + " plus " + integer2);
		}else{
			
			answer = integer1 - integer2;
			talker.speak("What is " + integer1 + " minus " + integer2, TextToSpeech.QUEUE_ADD, null);
			question.setText("What is " + integer1 + " minus " + integer2);
		}
	}
}
