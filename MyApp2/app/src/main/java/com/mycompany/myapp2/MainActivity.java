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
	CheckBox listenMode;
	CheckBox speakMode;
	CheckBox addMode;
	CheckBox multiMode;
	TextView maxAddInput;
	TextView maxMultiInput;
	AlertDialog.Builder builder;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	int answer;
	int integer1;
	int integer2;
	Intent intent;
	int maxSizeDefault = 20;
	boolean addition;
	boolean reverse;
	Random generator = new Random();
	long score = 0;
	TextToSpeech talker;
	boolean active;
	long addScore = 1;
	Locale myLang = Locale.UK;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		builder = new AlertDialog.Builder(this);  
		talker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {public void onInit(int status)
				{}
			});
		final Button button = findViewById(R.id.button1);
		final Button button2 = findViewById(R.id.button2);
		final Button button3 = findViewById(R.id.button3);
		answerInput = findViewById(R.id.editAnswer);
		question = findViewById(R.id.textView1);
		question.setText("Created");
		listenMode = findViewById(R.id.checkBoxRecog);
		speakMode = findViewById(R.id.checkBoxSpeak);
		addMode = findViewById(R.id.checkBoxAdd);
		multiMode = findViewById(R.id.checkBoxMulti);
		maxAddInput = findViewById(R.id.editMaxAdd);
		maxMultiInput = findViewById(R.id.editMaxMulti);
		//talker.speak("Welcome Sam to the maths quiz, press Go for a question", TextToSpeech.QUEUE_ADD, null);
		maxAddInput.setText(Integer.toString(maxSizeDefault));
		maxMultiInput.setText(Integer.toString(maxSizeDefault/2));
		button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{
					if (active)
					{
						//do nothing - 
					}
					else
					{
						active = true;
						say("Ready... Steady... Go!");  
						active = true;
						askQuestion();
					}
				}
			});

		button2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{
					if (active)
					{
						//intent.cancel();
						if (speakMode.isChecked())
						{
				    		talker.stop();
						}
						say("Thanks for playing! Your score was " + score);	
						active = false;
						score = 0;
						addScore = 1;
					}
				}
			});
		button3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v)
				{
					if (active && !listenMode.isChecked())
					{
						checkAnswer();
						//askQuestion();
						answerInput.setText("");
					}
					else
					{
						//do nothing
					}
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
		question.setText("Ready");
		waitABit(50);

	}

	private void waitABit(int timeWait)
	{

		try
		{
			Thread.sleep(timeWait);
		}
		catch (InterruptedException e)
		{}
	}

	private void waitToFinishTalking()
	{

		while (talker.isSpeaking())
		{
			waitABit(100);
		}
	}

	private void askQuestion()
	{

		doQuestion();
		waitToFinishTalking();
		if (listenMode.isChecked())
		{
			askSpeechInput();
		}
		else
		{

		}

	}

	// Showing google speech input dialog
    private void askSpeechInput()
	{
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-GB");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
						question.getText());
        try
		{
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
		catch (ActivityNotFoundException a)
		{

        }
    }

    // Receiving speech input

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
		{
            case REQ_CODE_SPEECH_INPUT: {
					if (resultCode == RESULT_OK && null != data)
					{

						ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
						int ii = 0;
						boolean numberFound = false;
						while (!numberFound && ii < result.size())
						{
							answerInput.setText(result.get(ii));
							try
							{
								Integer.parseInt(answerInput.getText().toString());
								numberFound = true;
							}
							catch (NumberFormatException e)
							{
								ii++;
							}
						}
						//answerInput.draw();
						waitABit(500);
						if (active)
						{
							checkAnswer();
						}
					}
					else
					{
						if (active)
						{
							askSpeechInput();
						}
					}
					break;
				}

        }

	}

	protected void checkAnswer()
	{
		if (active)
		{
			try
			{
				if (answer == Integer.parseInt(answerInput.getText().toString().replace("—", "-").replace(" ", "")))
				{
					score += addScore;
					addScore = addScore * 2;
					say("Correct! Your score is " + score);
				}
				else
				{
					addScore = 1;
					say("Not quite " + question.getText().toString() + " equals " + answer);
				}
			}
			catch (NumberFormatException e)
			{
				//Not a number
				say("Sorry " + answerInput.getText() + " wasn't recognised as a number."); 
			}
			waitToFinishTalking();
			if (active)
			{askQuestion();}
		}
	}

	protected void say(String sayMe){
		
		if (speakMode.isChecked())
		{
			talker.setLanguage(myLang);
			talker.speak(sayMe, TextToSpeech.QUEUE_ADD, null);
		}
		else
		{
			//Should probably have something
			Toast.makeText(getApplicationContext(),sayMe,Toast.LENGTH_SHORT).show();  
		}  
	}
	
	protected void doQuestion()
	{
		if(addMode.isChecked() && multiMode.isChecked()){
			addition = generator.nextBoolean();
		}else{
			addition = ! multiMode.isChecked();
		}
		reverse = generator.nextBoolean();

		if (addition)
		{
			int maxSize = maxSizeDefault;
			try{
				maxSize = Integer.parseInt(maxAddInput.getText().toString());
			}catch(NumberFormatException e)
			{
				maxSize = maxSizeDefault;
			}
			integer1 = generator.nextInt(maxSize);
			integer2 = generator.nextInt(maxSize);
			if(reverse){
				answer = integer1 - integer2;
				say("What is " + integer1 + " minus " + integer2);
				question.setText(integer1 + " minus " + integer2);
				
			}else{
				answer = integer1 + integer2;
				say("What is " + integer1 + " plus " + integer2);
				question.setText(integer1 + " plus " + integer2);
			}
		}
		else
		{
//multiplication mode
			
			int maxSize = maxSizeDefault/2;
			try{
				maxSize = Integer.parseInt(maxMultiInput.getText().toString());
			}catch(NumberFormatException e)
			{
				maxSize = maxSizeDefault/2;
			}
			integer1 = generator.nextInt(maxSize);
			integer2 = generator.nextInt(maxSize);
			if(integer1 * integer2 == 0){
				integer1 = integer1+1;
				integer2 = integer2+1;
			}
			if(reverse){
				answer = integer2;
				say("What is " + integer1 * integer2 + " divided by " + integer1);
				question.setText(integer1*integer2 + " divided by " + integer1);

			}else{
				answer = integer1 * integer2;
				say("What is " + integer1 + " times " + integer2);
				question.setText(integer1 + " times " + integer2);
			}
			}
	}
}
